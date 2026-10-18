package usc.etse.editoresymazmorras.dto;

import java.util.List;
import usc.etse.editoresymazmorras.model.TipoElemento;

// Una casilla tal como la ve el frontend (objetos solo si es COFRE).
// Se usa tanto para leer el mapa (GET) como para guardarlo (PUT): mismo formato ida y vuelta.
public record CeldaDTO(int x, int y, int planta, TipoElemento tipo, List<String> objetos) {
}
