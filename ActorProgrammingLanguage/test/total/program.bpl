d = (1 to 100) foreach (func a -> 
	(1 to 100) foreach (func b -> 
	(1 to 100) foreach (func c -> 
	if ((a<b) && (b<c) && (a^2 + b^2 = c^2)) then {println([a,' ', b, ' ', c]); return 0;} else {return 0;})));