package backend.academy;

import backend.academy.transformations.Transformation;
import java.util.List;

public interface CreatorsFlame {
    void create(FractalImage fractal, int numberIter, int numberSymmetry, List<Transformation> transformations);
}
