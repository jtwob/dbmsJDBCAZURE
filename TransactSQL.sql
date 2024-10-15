CREATE PROCEDURE InsertFacultyWithDeptSalary
    @fid INT,
    @fname NVARCHAR(50),
    @deptid INT
AS
BEGIN
    DECLARE @salary DECIMAL(10, 2);

    -- use cases to determine new salary rate
    SET @salary = CASE
        WHEN (SELECT AVG(salary) FROM Faculty WHERE deptid = @deptid) > 50000
            THEN 0.9 * (SELECT AVG(salary) FROM Faculty WHERE deptid = @deptid)
        WHEN (SELECT AVG(salary) FROM Faculty WHERE deptid = @deptid) < 30000
            THEN (SELECT AVG(salary) FROM Faculty WHERE deptid = @deptid)
        ELSE
            0.8 * (SELECT AVG(salary) FROM Faculty WHERE deptid = @deptid)
    END;

    -- insert new faculty
    INSERT INTO Faculty (fid, fname, deptid, salary)
    VALUES (@fid, @fname, @deptid, @salary);
END;
GO

CREATE PROCEDURE InsertFacultyWithAvgSalaryExcludingDept
    @fid INT,
    @fname NVARCHAR(50),
    @deptid INT,
    @exclude_deptid INT -- excluded deptid
AS
BEGIN
    DECLARE @avg_salary DECIMAL(10, 2);

    -- set avg to avg without excluded
    SET @avg_salary = (SELECT AVG(salary) 
                       FROM Faculty 
                       WHERE deptid != @exclude_deptid);

    -- insert new faculty
    INSERT INTO Faculty (fid, fname, deptid, salary)
    VALUES (@fid, @fname, @deptid, @avg_salary);
END;
GO

CREATE PROCEDURE GetAllFaculty
AS
BEGIN
    -- get all faculty
    SELECT fid, fname, deptid, salary
    FROM Faculty
    ORDER BY deptid, fname;
END;
GO