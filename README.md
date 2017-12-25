BasicProgrammingLanguage
===================

A general purpose programming language interpreted with a LL(1) parser.

Examples
--------
### Fibonnaci
```
f = func a -> 
  if a = 0 then 0 
  else if a = 1 then 1 
  else f (a-1) + f (a-2);
```
### Explicit Mutability
```
f = 10; 
f = 5; // Error!
```
```
mut f = 10; 
f = 5; // works
```
### Project Euler Problem 6
```
/*
The sum of the squares of the first ten natural numbers is,
12 + 22 + ... + 102 = 385
The square of the sum of the first ten natural numbers is,
(1 + 2 + ... + 10)2 = 552 = 3025
Hence the difference between the sum of the squares of the first ten natural numbers and the square of the sum is 3025 − 385 = 2640.
Find the difference between the sum of the squares of the first one hundred natural numbers and the square of the sum.
*/
sum = func a b -> a+b;
sumOfSquares = (1 to 10) foldl func a b -> a + b^2;
squareOfSums = ((1 to 10) foldl func a b -> a + b)^2;
diff = squareOfSums - sumOfSquares;
print(diff); //2640
```
### Definable binary keyword functions
```
a = [3,2,4] map func a -> a*2;
b = map([3,2,4], func a -> a*2);
print(a = b); // true

// any keyword can be defined by the programmer
sum = func a b -> a + b; 
a = 6 sum 4;

// even certain non-alphabetical operators
!! = func a b -> a{b}; // use !! as index operator
a = [1,2,3] !! 0;
```
### Datatypes
```
datatype Rectangle = Rect{width, height} | Square {size};
b = new Rectangle.Square(size=4);
```
