package backend.academy.multithreaded;

import backend.academy.Changer;
import backend.academy.Creator;
import backend.academy.CreatorsFlame;
import backend.academy.FractalImage;
import backend.academy.transformations.AthenianTransformation;
import backend.academy.transformations.Transformation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class MultithreadedCreatorFlame implements CreatorsFlame {

    @Override
    public void create(FractalImage fractal, int numberIter, int numberSymmetry, List<Transformation> transformations) {
        Set<AthenianTransformation> athen = getAthen(transformations.size());
        CountDownLatch latch = new CountDownLatch(transformations.size());
        for (Transformation trans : transformations) {
            Thread.ofVirtual()
                .start(() -> {
                    try {
                        new Creator(fractal, new Changer(trans), athen, numberIter, numberSymmetry).create();
                    } finally {
                        latch.countDown();
                    }
                });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(e.getMessage());
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
