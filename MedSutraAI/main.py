from fastapi import FastAPI, UploadFile, Form, File
from fastapi.responses import JSONResponse
import cv2
import numpy as np
from vision import extract_features, verify_match
import uvicorn
import logging

app = FastAPI(title="Medicine Verification API")
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@app.post("/verify-medicine")
async def verify_medicine(
    expected_name: str = Form(...),
    image: UploadFile = File(...)
):
    try:
        contents = await image.read()
        nparr = np.frombuffer(contents, np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
        
        if img is None:
            return JSONResponse(status_code=400, content={"error": "Invalid image file"})
            
        logger.info(f"Verifying medicine. Expected: {expected_name}")
        
        # Extract features
        features = extract_features(img)
        logger.info(f"Extracted features: {features}")
        
        # Verify match against expected name
        is_match, confidence, detected_name = verify_match(features, expected_name)
        
        result = "MATCH" if is_match else "MISMATCH"
        
        return {
            "detected_name": detected_name,
            "confidence": confidence,
            "result": result,
            "message": "Correct medicine" if is_match else "Medicine mismatch detected"
        }
    except Exception as e:
        logger.error(f"Error processing image: {e}")
        return JSONResponse(status_code=500, content={"error": str(e)})

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
