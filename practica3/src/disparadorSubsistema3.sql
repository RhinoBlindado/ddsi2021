-- Disparador para que no se permita que una entidad colaboradoradora sea patrocinadora en una misma edición

CREATE OR REPLACE TRIGGER EntidadUnicaPorEdicion 
	BEFORE 
	INSERT ON patrocina
	FOR EACH ROW
DECLARE
	colaboradores_edicion INTEGER;
BEGIN
	SELECT count(*) INTO colaboradores_edicion FROM colabora WHERE idEntidad = :new.idEntidad AND anno = :new.anno;
	IF (colaboradores_edicion != 0) THEN 
		raise_application_error(-20600, :new.idEntidad || 'Un colaborador no puede ser patrocinador en la misma edición');
	END IF;
END; 
