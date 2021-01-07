-- Subsistema 5 - Materiales / Pedidos
-- Disparador para que no se permita pedir en un pedido materiales de diferentes patrocinadores

CREATE OR REPLACE TRIGGER pedidoMateriales
	BEFORE 
	INSERT OR UPDATE ON PIDE
	FOR EACH ROW
DECLARE 
	num INTEGER;
BEGIN
	SELECT COUNT(*) INTO num FROM PIDE WHERE PIDE.numPedido = :new.numPedido AND (SELECT idEntidad FROM MATERIAL_PROPORCIONADO WHERE idMaterial = :new.IdMaterial) != (SELECT idEntidad FROM MATERIAL_PROPORCIONADO WHERE idMaterial = PIDE.IdMaterial);
	IF ( num != 0) THEN
		RAISE_APPLICATION_ERROR(-20600, :new.numPedido || ' Un pedido sólo puede tener materiales del mismo patrocinador');
	END IF ;
END;



