package backend.academy.singlethreaded;

import backend.academy.Changer;
import backend.academy.Creator;
import backend.academy.CreatorsFlame;
import backend.academy.FractalImage;
import backend.academy.transformations.AthenianTransformation;
import backend.academy.transformations.Transformation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreatorFlame implements CreatorsFlame {
    @Override
    public void create(FractalImage fractal, int numberIter, int numberSymmetry, List<Transformation> transformations) {
        Set<AthenianTransformation> athen = getAthen(transformations.size());
        for (Transformation trans : transformations) {
            new Creator(fractal, new Changer(trans), athen, numberIter, numberSymmetry).create();
        }
    }

    private Set<AthenianTransformation> getAthen(int n) {
        Set<AthenianTransformation> athenSet = new HashSet<>();
        for (int i = 0; i < n; i++) {
            athenSet.add(AthenianTransformation.create());
        }
        return athenSet;
    }
}
