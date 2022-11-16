CREATE PROCEDURE `sp_get_patient_programs`()
BEGIN
    declare scName varchar(250);
    declare q varchar(2000);
    
    DROP TABLE IF EXISTS rt_patientprograms;
    create table rt_patientprograms (
      patient_id int,
      cccnumber varchar(200),
      birthdate date,
      program varchar(200),
      date_enrolled date,
      date_completed date,
      outcome varchar(200),
      location_id int,
      MFLCode varchar(200)
      
    );
     DROP TABLE IF EXISTS MySchemaNames;
    create table MySchemaNames (
        schemaName varchar(250)
    );
     DROP TABLE IF EXISTS MySchemaNames;
    create table MySchemaNames (
        schemaName varchar(250)
    );
insert into MySchemaNames
    SELECT CONCAT(SCHEMA_NAME) AS DBNames
FROM `information_schema`.`SCHEMATA`
WHERE SCHEMA_NAME LIKE 'openmrsm%';
label1:
LOOP
        set scName = (select schemaName from MySchemaNames limit 1);
        set @q = concat('insert into rt_patientprograms(patient_id,cccnumber,birthdate,program,date_enrolled,date_completed,outcome,MFLCODE) 
			SELECT 
			pp.patient_id,
            case when pii.identifier_type=6 then  pii.identifier else null end CCCnumber,
            pe.birthdate,
			p.name program,
			pp.date_enrolled,
			pp.date_completed,
			pp.outcome_concept_id outcome,
			locatt.value_reference AS MFLCode
            From ', scname,'.patient_program pp
            left JOIN ', scname,'.patient_identifier pii on pii.patient_id = pp.patient_id
            INNER JOIN ', scname,'.person pe on pe.person_id = pp.patient_id
        INNER JOIN ', scname,'.program p  ON pp.program_id=p.program_id
	inner JOIN   ', scname,'.location loc ON loc.location_id = (select property_value FROM  ', scname,'.global_property g where property=''kenyaemr.defaultLocation'')
	LEFT JOIN   ', scname,'.location_attribute locatt ON loc.location_id = locatt.location_id
	LEFT JOIN   ', scname,'.location_attribute_type locatttype ON locatttype.location_attribute_type_id = locatt.attribute_type_id
	WHERE locatttype.name = ''Master Facility Code''
    group by patient_id,program
');
PREPARE stmt1 FROM @q;
        EXECUTE stmt1;
        DEALLOCATE PREPARE stmt1;

        delete from MySchemaNames where schemaName = scName;
        IF ((select count(*) from MySchemaNames) > 0) THEN
            ITERATE label1;
        END IF;
        LEAVE label1;

    END LOOP label1;

    SELECT * FROM rt_patientprograms;

END