
USE kec_academic;

-- Add 10 sample students
INSERT INTO students
(roll_no, student_name, department, current_semester)
VALUES
('25ITR183', 'Arun Kumar', 'IT', 3),
('25ITR184', 'Priya S', 'CSE', 3),
('25ITR185', 'Karthik R', 'ECE', 3),
('25ITR186', 'Divya M', 'IT', 3),
('25ITR187', 'Rahul S', 'CSE', 3),
('25ITR188', 'Nithya R', 'ECE', 3),
('25ITR189', 'Vignesh K', 'IT', 3),
('25ITR190', 'Keerthana P', 'CSE', 3),
('25ITR191', 'Santhosh B', 'ECE', 3),
('25ITR192', 'Harini V', 'IT', 3);

-- Add course enrollment and attendance
INSERT INTO enrollments
(roll_no, course_code, semester, total_classes,
 classes_attended, medical_condonation)
VALUES
('25ITR183', '24ITR31', 3, 50, 42, 0),
('25ITR184', '24ITR31', 3, 50, 45, 0),
('25ITR185', '24ITR31', 3, 50, 40, 0),
('25ITR186', '24ITR31', 3, 50, 44, 0),
('25ITR187', '24ITR31', 3, 50, 38, 0),
('25ITR188', '24ITR31', 3, 50, 47, 0),
('25ITR189', '24ITR31', 3, 50, 41, 0),
('25ITR190', '24ITR31', 3, 50, 46, 0),
('25ITR191', '24ITR31', 3, 50, 39, 0),
('25ITR192', '24ITR31', 3, 50, 43, 0);

-- Add assessment marks using actual enrollment IDs
INSERT INTO assessment_marks
(enrollment_id, cia1, cia2, assignment_quiz_lab,
 ese_marks, ese_absent)
SELECT
    e.enrollment_id,
    CASE s.roll_no
        WHEN '25ITR183' THEN 75
        WHEN '25ITR184' THEN 82
        WHEN '25ITR185' THEN 70
        WHEN '25ITR186' THEN 88
        WHEN '25ITR187' THEN 65
        WHEN '25ITR188' THEN 91
        WHEN '25ITR189' THEN 78
        WHEN '25ITR190' THEN 85
        WHEN '25ITR191' THEN 72
        WHEN '25ITR192' THEN 90
    END,
    CASE s.roll_no
        WHEN '25ITR183' THEN 80
        WHEN '25ITR184' THEN 85
        WHEN '25ITR185' THEN 76
        WHEN '25ITR186' THEN 90
        WHEN '25ITR187' THEN 70
        WHEN '25ITR188' THEN 94
        WHEN '25ITR189' THEN 82
        WHEN '25ITR190' THEN 88
        WHEN '25ITR191' THEN 75
        WHEN '25ITR192' THEN 92
    END,
    85, 78, 0
FROM enrollments e
JOIN students s ON s.roll_no = e.roll_no
WHERE s.roll_no BETWEEN '25ITR183' AND '25ITR192'
  AND e.course_code = '24ITR31';
