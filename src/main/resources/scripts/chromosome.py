import random
import sys
from random import randint


class MyChromosome:
    def __init__(self, problemsParameters=None):
        self.__problemsParameters = problemsParameters
        self.__fitness = 0.0
        self.__representation = []
        while not self.__representation:
            self.__init_representation()

    @property
    def representation(self):
        return self.__representation

    @property
    def fitness(self):
        return self.__fitness

    @representation.setter
    def representation(self, chromosome=None):
        if chromosome is None:
            chromosome = []
        self.__representation = chromosome

    @fitness.setter
    def fitness(self, fit=0.0):
        self.__fitness = fit

    def crossover(self, c):
        cut = randint(0, len(self.__representation) - 1)
        new_representation = [None] * len(self.__representation)
        for i in range(cut):
            new_representation[i] = self.__representation[i]
        for i in range(cut, len(self.__representation)):
            new_representation[i] = c.__representation[i]
        offspring = MyChromosome(c.__problemsParameters)
        offspring.representation = new_representation
        return offspring

    def mutation(self):
        first_position = randint(0, len(self.__representation) - 1)
        second_position = randint(0, len(self.__representation) - 1)
        while self.__representation[first_position] != self.__representation[second_position]:
            second_position = randint(0, len(self.__representation) - 1)
        first_value = self.__representation[first_position]
        self.__representation[first_position] = self.__representation[second_position]
        self.__representation[second_position] = first_value

    def __str__(self):
        return 'Chromosome: ' + str(self.__representation) + ' has fit: ' + str(self.__fitness) + '.'

    def __repr__(self):
        return self.__str__()

    def __eq__(self, c):
        return self.__representation == c.__representation and self.__fitness == c.__fitness

    def __init_representation(self):
        for i in range(len(self.__problemsParameters["furniture"])):
            self.__representation.append(randint(0, len(self.__problemsParameters["furniture"][
                                                            list(self.__problemsParameters["furniture"].keys())[
                                                                i]]) - 1))
        if self.__representation in self.__problemsParameters["generated"]:
            self.__representation = []