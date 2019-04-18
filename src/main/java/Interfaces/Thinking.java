package Interfaces;

import tags.Tuple;

import java.util.List;

public interface Thinking {
    // All layers: NN, KN, ES, META implement this interface.
// It is the “main method” for each layer. It initiates the
// thinking process for every layer and defines how each
// layer interacts with the other. The interface assumes
// the following abstract view of the method:
// output_to_next_layer think(input_to_current_layer)
    List<Tuple> think(int iterate, List<Tuple> tuples);
}
