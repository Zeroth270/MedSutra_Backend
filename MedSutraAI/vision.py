import cv2
import numpy as np
import pytesseract
import difflib
import os

# Set tesseract path for Windows
tesseract_path = r'C:\Program Files\Tesseract-OCR\tesseract.exe'
if os.path.exists(tesseract_path):
    pytesseract.pytesseract.tesseract_cmd = tesseract_path

import re

def process_image_strategies(gray):
    extracted_texts = []
    
    # --- Strategy A: CLAHE + PSM 3 ---
    clahe = cv2.createCLAHE(clipLimit=3.0, tileGridSize=(8,8))
    gray_clahe = clahe.apply(gray)
    text_clahe = pytesseract.image_to_string(gray_clahe, config='--psm 3')
    extracted_texts.append(text_clahe)
    
    # --- Strategy B: Large Block Adaptive Thresholding + PSM 3 ---
    median = cv2.medianBlur(gray, 3)
    thresh_adapt = cv2.adaptiveThreshold(median, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, 
                                   cv2.THRESH_BINARY, 81, 15)
    text_adapt = pytesseract.image_to_string(thresh_adapt, config='--psm 3')
    extracted_texts.append(text_adapt)
    
    # --- Strategy C: Sharpening + Otsu + PSM 6 ---
    kernel = np.array([[-1,-1,-1], [-1,9,-1], [-1,-1,-1]])
    sharpened = cv2.filter2D(gray, -1, kernel)
    blur = cv2.GaussianBlur(sharpened, (5,5), 0)
    _, thresh_otsu = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
    text_otsu = pytesseract.image_to_string(thresh_otsu, config='--psm 6')
    extracted_texts.append(text_otsu)
    
    return " ".join(extracted_texts)

def extract_features(img):
    """
    Extracts text from the medicine packaging image using OCR.
    Uses multi-scale processing and ROTATION INVARIANCE to hit maximum extraction limits.
    """
    features = {
        "text": ""
    }
    
    # Resize significantly to make small printed text readable
    img = cv2.resize(img, None, fx=3, fy=3, interpolation=cv2.INTER_CUBIC)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    
    all_texts = []
    
    # Run the extraction on 4 rotations (0, 90, 180, 270 degrees).
    # Tesseract fails completely if the text is vertical or upside down.
    rotations = [
        None,
        cv2.ROTATE_90_CLOCKWISE,
        cv2.ROTATE_180,
        cv2.ROTATE_90_COUNTERCLOCKWISE
    ]
    
    for rot in rotations:
        if rot is not None:
            rotated_gray = cv2.rotate(gray, rot)
        else:
            rotated_gray = gray
            
        text = process_image_strategies(rotated_gray)
        all_texts.append(text)
        
    combined_text = " ".join(all_texts)
    
    # Clean text: remove all non-alphanumeric characters except spaces
    cleaned = re.sub(r'[^A-Za-z0-9\s]', ' ', combined_text)
    
    # Collapse multiple spaces and uppercase
    clean_text = " ".join(cleaned.split()).upper()
    features["text"] = clean_text
    
    return features

def verify_match(features, expected_name):
    """
    Compares the extracted packaging text to the expected medicine name.
    Uses a robust character-level sliding window to find fuzzy matches hidden inside large blocks of text.
    Returns (is_match, confidence_score, detected_name)
    """
    expected_name_upper = expected_name.upper().strip()
    extracted_text = features.get("text", "")
    
    # If text is empty, no match
    if not extracted_text or len(extracted_text) < 3:
        return False, 0.0, "No readable text"
        
    flat_extracted = extracted_text.replace(" ", "")
    flat_expected = expected_name_upper.replace(" ", "")
    
    if flat_expected in flat_extracted:
        return True, 100.0, expected_name
        
    max_confidence = 0.0
    best_match_phrase = ""
    
    # Character-level sliding window
    # Sizes: -1 (missing char), 0 (exact), +1 (extra char), +2 (two extra chars)
    base_len = len(flat_expected)
    window_sizes = [max(1, base_len - 1), base_len, base_len + 1, base_len + 2]
    
    for w_size in window_sizes:
        if w_size > len(flat_extracted):
            continue
            
        for i in range(len(flat_extracted) - w_size + 1):
            window_str = flat_extracted[i:i+w_size]
            similarity = difflib.SequenceMatcher(None, flat_expected, window_str).ratio()
            confidence = similarity * 100
            
            if confidence > max_confidence:
                max_confidence = confidence
                best_match_phrase = window_str

    # Threshold for a positive match
    if max_confidence >= 75.0: # 75% character match inside the sliding window
        return True, max_confidence, expected_name
    else:
        # Fallback output for the user
        extracted_words = extracted_text.split()
        if len(extracted_words) > 0:
            longest_words = sorted(extracted_words, key=len, reverse=True)[:3]
            fallback_name = " ".join(longest_words)
        else:
            fallback_name = "Unrecognized text"
            
        return False, max_confidence, fallback_name
