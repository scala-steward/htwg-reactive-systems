#!/usr/bin/env python

import random
from datetime import date


def random_name():
    first_names = ["John", "Jane", "Corey", "Travis", "Dave", "Kurt", "Neil", "Sam", "Steve", "Tom", "James", "Robert",
                   "Michael", "Charles", "Joe", "Mary", "Maggie", "Nicole", "Patricia", "Linda", "Barbara", "Elizabeth",
                   "Laura", "Jennifer", "Maria"]
    last_names = ["Smith", "Doe", "Jenkins", "Robinson", "Davis", "Stuart", "Jefferson", "Jacobs", "Wright",
                  "Patterson", "Wilks", "Arnold", "Calvin", "Newman", "Hayes", "Russel", "Thomas", "Lee", "Walker",
                  "Allen", "Young", "Hernandez", "King", "Wright", "Lopez"]
    first_name = random.choice(first_names)
    last_name = random.choice(last_names)
    return f'{first_name} {last_name}'


# Sample data
movie_names = ["The Shawshank Redemption", "The Godfather", "Pulp Fiction", "The Dark Knight", "Forrest Gump"]
dates = [date(2022, 1, 15), date(2022, 2, 20), date(2022, 3, 25), date(2022, 4, 30), date(2022, 5, 5)]

# Generate example data
for _ in range(1000):
    movie_name = random.choice(movie_names)
    reviewer = random_name()
    if random.random() > 0.2:
        rating = f"{random.randint(1, 5)} Stars"
    else:
        rating = f"{random.randint(1, 100)}%"
    review_date = random.choice(dates)

    has_date = random.random() > 0.9
    if has_date:
        example_data = f'"{movie_name}" rated {rating} by "{reviewer}"'
    else:
        example_data = f'"{movie_name}" rated {rating} by "{reviewer}" on "{review_date.strftime("%Y-%m-%d")}"'
    print(example_data)
