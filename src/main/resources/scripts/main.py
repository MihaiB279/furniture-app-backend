import sys

from chromosome import MyChromosome
from genetic_algorithm import GA
from utils import get_list, get_index_off_generated
from utils_ga import get_right_furniture, evaluation


def find_furniture(furniture, already_generated, budget, population_size=300, no_of_generations=100,
                   evaluation_function=evaluation, chromosome=MyChromosome):
    genetic_parameters = {
        "popSize": population_size,
        "noGen": no_of_generations,
        "function": evaluation_function,
        "chromosome": chromosome
    }
    problem_parameters = {
        "furniture": furniture,
        "budget": budget,
        "generated": already_generated
    }
    ga = GA(genetic_parameters, problem_parameters)
    ga.initialisation()
    ga.evaluation()
    bests = []
    last_gens = []

    for generation in range(genetic_parameters["noGen"]):
        # ga.oneGeneration()
        ga.oneGenerationElitism()
        # ga.oneGenerationSteadyState()
        best_chromosome = ga.bestChromosome()
        last_gens = ga.population
        if best_chromosome not in bests:
            bests.append(best_chromosome)

    the_best = bests[0]
    for best in bests:
        if best.fitness < the_best.fitness:
            the_best = best

    the_bests = [the_best]

    for candidate in last_gens:
        if candidate.fitness < the_bests[0].fitness:
            the_bests = [candidate]
        elif candidate.fitness == the_best.fitness and candidate not in the_bests:
            the_bests.append(candidate)

    return the_bests


if __name__ == "__main__":
    number_of_furniture = int(sys.stdin.readline())
    furniture_wishes = {}
    for step in range(number_of_furniture):
        furniture_type = sys.stdin.readline().strip()
        details = sys.stdin.readline().strip()
        furniture_wishes[furniture_type] = get_list(details)
    budget = float(sys.stdin.readline().strip())
    already_generated_furniture = []
    big_list = int(sys.stdin.readline())
    small_list = int(sys.stdin.readline())
    for big_step in range(big_list):
        single_list = []
        for small_step in range(small_list):
            furniture_type = sys.stdin.readline().strip()
            name = sys.stdin.readline().strip()
            company = sys.stdin.readline().strip()
            single_list.append({
                'furniture_type': furniture_type,
                'name': name,
                'company': company
            })
        already_generated_furniture.append(single_list)
    furniture_available = get_right_furniture(furniture_wishes)
    if type(furniture_available) == str:
        sys.stdout.write(furniture_available + '\n')
    else:
        index_list_generated = get_index_off_generated(already_generated_furniture, furniture_available)
        result = find_furniture(furniture_available, index_list_generated, budget, 1000, 500)
        final_res = result[0]
        pos = 0
        for type_f in furniture_available.keys():
            sys.stdout.write(str(furniture_available[type_f][final_res.representation[pos]]) + '\n')
            pos = pos + 1

    sys.stdout.flush()
    sys.exit(0)
