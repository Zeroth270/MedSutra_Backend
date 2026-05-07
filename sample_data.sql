-- MedSutra Sample Data
-- This script contains sample data mapped to the current database structure.
-- It can be used to populate the database for testing and frontend development.

-- 1. Users
-- Note: Passwords are encrypted with BCrypt. We use a placeholder hash here.
INSERT INTO users (name, email, password, role, phone, age, state, annual_income, ration_card_type, family_size, occupation, created_at) VALUES
('John Doe', 'patient1@example.com', '$2a$10$dummyHash', 'PATIENT', '1234567890', 45, 'Delhi', 150000, 'BPL', 4, 'Laborer', NOW()),
('Jane Smith', 'patient2@example.com', '$2a$10$dummyHash', 'PATIENT', '0987654321', 60, 'Maharashtra', 300000, 'APL', 2, 'Retired', NOW()),
('Dr. Adams', 'doctor@example.com', '$2a$10$dummyHash', 'DOCTOR', '1112223333', 40, 'Delhi', 800000, NULL, NULL, 'Doctor', NOW()),
('Alice Care', 'caregiver@example.com', '$2a$10$dummyHash', 'CAREGIVER', '4445556666', 30, 'Delhi', 400000, NULL, NULL, 'Teacher', NOW());

-- 2. Doctors
INSERT INTO doctors (user_id, specialization, hospital_name, experience_years) VALUES
((SELECT id FROM users WHERE email = 'doctor@example.com'), 'Cardiologist', 'AIIMS Delhi', 15);

-- 3. PatientRelationships
INSERT INTO patient_relationships (patient_id, caregiver_id, doctor_id, relationship_type) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), (SELECT id FROM users WHERE email = 'caregiver@example.com'), (SELECT id FROM users WHERE email = 'doctor@example.com'), 'PRIMARY'),
((SELECT id FROM users WHERE email = 'patient2@example.com'), NULL, (SELECT id FROM users WHERE email = 'doctor@example.com'), 'PRIMARY');

-- 4. MedicineReference
INSERT INTO medicine_reference (name, color, shape, size, imprint_text) VALUES
('Paracetamol', 'White', 'Round', 'Small', 'PARA 500'),
('Metformin', 'White', 'Oval', 'Medium', 'MET 500'),
('Amlodipine', 'Yellow', 'Round', 'Small', 'AML 5');

-- 5. Medications
INSERT INTO medications (patient_id, name, dosage, time, frequency, start_date, end_date) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), 'Paracetamol', '500mg', '08:00:00', 'DAILY', CURRENT_DATE - INTERVAL '30 days', CURRENT_DATE + INTERVAL '30 days'),
((SELECT id FROM users WHERE email = 'patient1@example.com'), 'Amlodipine', '5mg', '20:00:00', 'DAILY', CURRENT_DATE - INTERVAL '15 days', CURRENT_DATE + INTERVAL '45 days'),
((SELECT id FROM users WHERE email = 'patient2@example.com'), 'Metformin', '500mg', '09:00:00', 'TWICE_DAILY', CURRENT_DATE - INTERVAL '60 days', CURRENT_DATE + INTERVAL '365 days');

-- 6. MedicationLogs
INSERT INTO medication_logs (medication_id, taken, taken_time, delay_minutes) VALUES
((SELECT med_id FROM medications WHERE name = 'Paracetamol' AND patient_id = (SELECT id FROM users WHERE email = 'patient1@example.com')), true, NOW() - INTERVAL '1 day', 5),
((SELECT med_id FROM medications WHERE name = 'Paracetamol' AND patient_id = (SELECT id FROM users WHERE email = 'patient1@example.com')), true, NOW() - INTERVAL '2 days', 0),
((SELECT med_id FROM medications WHERE name = 'Paracetamol' AND patient_id = (SELECT id FROM users WHERE email = 'patient1@example.com')), false, NULL, NULL),
((SELECT med_id FROM medications WHERE name = 'Amlodipine' AND patient_id = (SELECT id FROM users WHERE email = 'patient1@example.com')), true, NOW() - INTERVAL '1 day', 15),
((SELECT med_id FROM medications WHERE name = 'Metformin' AND patient_id = (SELECT id FROM users WHERE email = 'patient2@example.com')), true, NOW() - INTERVAL '1 day', 0);

-- 7. Reminders
INSERT INTO reminders (patient_id, medication_id, message, reminder_time, frequency, active, created_at) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), (SELECT med_id FROM medications WHERE name = 'Paracetamol' AND patient_id = (SELECT id FROM users WHERE email = 'patient1@example.com')), 'Time to take Paracetamol', '08:00:00', 'DAILY', true, NOW()),
((SELECT id FROM users WHERE email = 'patient2@example.com'), (SELECT med_id FROM medications WHERE name = 'Metformin' AND patient_id = (SELECT id FROM users WHERE email = 'patient2@example.com')), 'Time for your Metformin dose', '09:00:00', 'TWICE_DAILY', true, NOW());

-- 8. Alerts
INSERT INTO alerts (patient_id, message, urgency, status, created_at) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), 'Missed Paracetamol dose yesterday', 'HIGH', 'UNREAD', NOW() - INTERVAL '12 hours'),
((SELECT id FROM users WHERE email = 'patient2@example.com'), 'Upcoming appointment reminder', 'LOW', 'READ', NOW() - INTERVAL '2 days');

-- 9. MoodLogs
INSERT INTO mood_logs (patient_id, before_mood, after_mood, symptom_score, timestamp) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), 3, 4, 5, NOW() - INTERVAL '1 day'),
((SELECT id FROM users WHERE email = 'patient1@example.com'), 2, 3, 7, NOW() - INTERVAL '3 days'),
((SELECT id FROM users WHERE email = 'patient2@example.com'), 4, 5, 2, NOW() - INTERVAL '1 day');

-- 10. EnvironmentData
INSERT INTO environment_data (patient_id, aqi, temperature, humidity, location, timestamp) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), 150.5, 30.2, 60.0, 'Delhi', NOW() - INTERVAL '1 hour'),
((SELECT id FROM users WHERE email = 'patient1@example.com'), 160.0, 31.0, 58.0, 'Delhi', NOW() - INTERVAL '1 day'),
((SELECT id FROM users WHERE email = 'patient2@example.com'), 80.0, 28.5, 65.0, 'Mumbai', NOW() - INTERVAL '2 hours');

-- 11. VerificationLogs
INSERT INTO verification_logs (patient_id, medication_id, detected_name, confidence_score, result, timestamp) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), (SELECT med_id FROM medications WHERE name = 'Paracetamol' AND patient_id = (SELECT id FROM users WHERE email = 'patient1@example.com')), 'Paracetamol', 95.5, 'MATCH', NOW() - INTERVAL '1 day'),
((SELECT id FROM users WHERE email = 'patient1@example.com'), (SELECT med_id FROM medications WHERE name = 'Amlodipine' AND patient_id = (SELECT id FROM users WHERE email = 'patient1@example.com')), 'Amlodipine', 88.0, 'MATCH', NOW() - INTERVAL '5 days');

-- 12. AIResults
INSERT INTO ai_results (patient_id, adherence_score, risk_level, prediction, created_at) VALUES
((SELECT id FROM users WHERE email = 'patient1@example.com'), 85.5, 'MODERATE', 'Patient occasionally misses doses. Moderate risk of non-adherence.', NOW()),
((SELECT id FROM users WHERE email = 'patient2@example.com'), 98.0, 'LOW', 'Excellent adherence. Low risk.', NOW());

-- 13. Hospitals (Note: DataSeeder might insert these if empty)
INSERT INTO hospitals (name, address, city, state, latitude, longitude, phone, ayushman_supported) VALUES
('Sample Care Hospital', '123 Main St', 'Delhi', 'Delhi', 28.5, 77.2, '1231231234', true),
('City General', '456 Center Ave', 'Pune', 'Maharashtra', 18.5, 73.8, '9876543210', false);

-- 14. MedicinePrices (Note: DataSeeder might insert these if empty)
INSERT INTO medicine_prices (medicine_name, brand_price, generic_name, generic_price, manufacturer) VALUES
('Aspirin', 50.0, 'Aspirin Generic', 10.0, 'Sample Pharma'),
('Atorvastatin', 300.0, 'Atorvastatin Generic', 80.0, 'Generic Corp');

-- 15. GovernmentSchemes (Note: DataSeeder might insert these if empty)
INSERT INTO government_schemes (scheme_name, eligibility_criteria, benefits, state) VALUES
('Sample Scheme', 'Low Income', 'Free Consultations', 'All India');
