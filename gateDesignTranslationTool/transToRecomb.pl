#! /usr/bin/perl -w
#Author: Jenhan Tao
#converts transcriptional designs from Swapnil's scripts to recombinase base designs or designs that use recombinase and transcriptional motifs
#Swapnil's designs are given in the format: expression\ndesign\ntruthTable. The truth table is the ouput column and is given as a hexadecimal character

#takes a hexadecimal as input and then returns 
sub convertHexToBinary {
if ($_[0] !~ /^[a-f_0-9]{1,2}$/ig) {
	die("Please give sub convertHexToBinary a valid hexadecimal\n");
}
my $toReturn = "";
for(my $i=0; $i<length($_[0]); $i++) {
	if(substr($_[0], $i,1) =~/[a]/i) {
		$toReturn = "1010".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[b]/i) {
		$toReturn = "1011".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[c]/i) {
		$toReturn = "1100".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[d]/i) {
		$toReturn = "1101".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[e]/i) {
		$toReturn = "1110".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[f]/i) {
		$toReturn = "1111".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[0]/i) {
		$toReturn = "0000".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[1]/i) {
		$toReturn = "0001".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[2]/i) {
		$toReturn = "0010".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[3]/i) {
		$toReturn = "0011".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[4]/i) {
		$toReturn = "0100".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[5]/i) {
		$toReturn = "0101".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[6]/i) {
		$toReturn = "0110".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[7]/i) {
		$toReturn = "0111".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[8]/i) {
		$toReturn = "1000".$toReturn;
	} elsif(substr($_[0], $i,1) =~/[9]/i) {
		$toReturn = "1001".$toReturn;
	} 
}
return $toReturn;
}


# TODO FINISH THIS!!!!!!
#input: string denoting a transcriptional NOR gate design
#output: an array containing all combinations of recombinase/transcriptional NOR gate designs
sub translateTransToRecombdesign { 

my @toReturn;
my @gates =($_[0] =~ /p\(\w+\) p\(\w+\) r g\(\w+\) t/ig); # stores each of the nor gates in an array
my @outputs = ($_[0] =~ /p\(\w+\) r \w+ t/ig); #stores all output operons
my @combinations = generateAllCombinations(scalar(@gates), 2);
	for(my$i = 0; $i<@combinations; $i++) {
$recombCount = 0;
		#print($combinations[$i]."\n");
		my $newDesign = "";		
		for(my $j=0; $j<@gates; $j++) {
			if(substr($combinations[$i],$j,1) =~ /1{1}/) { 
			#if 1, translate to recombinase design, if 0, just appened	
				$recombCount = $recombCount+1;	
				$newDesign = $newDesign.translateTransNORGate($gates[$j])." ";
			} else {
				$newDesign = $newDesign.$gates[$j]." ";
			}
		}
		for(my $k=0; $k<@outputs; $k++) {
		#just append outputs right now, maybe do differnt types of youput gates later
			$newDesign = $newDesign.$outputs[$k]." ";
		}
		push(@toReturn, $newDesign);	
	}	
return @toReturn;
}

#input: one string denoting a transcriptional NOR gate design
#output: one string denoting a recombinase NOR gate design
sub translateTransNORGate {
if(!$_[0] =~ /p\((\w+)\) p\((\w+)\) r g\((\w+)\) t/ig) {
	die "input is not a valid NOR gate\n";
}
@values = ($_[0] =~ /p\((\w+)\) p\((\w+)\) r g\((\w+)\) t/ig); #store the capturing groups
$toReturn = ">($values[0]) p <($values[0]) >($values[1]) p <($values[1]) r g(r$recombCount) t >(r$recombCount) p <(r$recombCount) r g($values[2]) t";
if($values[0] =~ /[abc]/i) {
	$toReturn = "p($values[0]) >($values[1]) p <($values[1]) r g(r$recombCount) t >(r$recombCount) p <(r$recombCount) r g($values[2]) t";
} elsif($values[1] =~ /[abc]/i) {
	$toReturn = ">($values[0]) p <($values[0]) p($values[1]) r g(r$recombCount) t >(r$recombCount) p <(r$recombCount) r g($values[2]) t";
} 
if($values[0] =~ /[abc]/i && $values[1] =~/[abc]/i) {
	$toReturn = "p($values[0]) p($values[1]) r g(r$recombCount) t >(r$recombCount) p <(r$recombCount) r g($values[2]) t";
}
return $toReturn;
}

#input: one string denoting a transcriptional output operon design
#output: one string denoting a recombinase output operon design
sub translateTransOutput {
if(!$_[0] =~ /p\(\w+\) r \w+ t/ig) {
	die "input is not a valid transcriptional NOR gate\n";
}
@values = ($_[0] =~ /p\((\w+)\) r (\w+) t/ig); #store the capturing groups
$toReturn = "p >($values[0]) t <($values[0]) r g($values[1]) t";
return $toReturn;
}


#recursively generate all combinations
#input: length of combinations, base of each digit- 0 would be 0's only, 1 would be 0's and 1's, 2 would be 0's, 1's, and 2's, etc
#output: an array of all possible combinations
my $length = 1;
my $base = 0;
sub generateAllCombinations {
$length = $_[0];
$base = $_[1];
if(@_ < 2) {
	die "generateAllCombinations requires a length argument and a base argument.\nPlease provide both.\n";
}
my @combinations;
for(my $i=0; $i<$base; $i++) {
	$combinations[$i] = $i;
}
if(length($combinations[0]) < $length) {
	#combination strings aren't quite long enough yet, do recursive call
	@combinations = genAllCombHelper(@combinations);
}
return @combinations;

}


#helper subroutine for generateAllCombinations
sub genAllCombHelper {
@combinations = @_;
my @toReturn;
for(my $i=0; $i<$base; $i++) {
	for(my$j=0; $j<@combinations; $j++) {
		push(@toReturn, ($_[$j].$i));
	}
}
if(length($toReturn[0]) < $length) {
	#combination strings aren't quite long enough yet, do recursive call
	@toReturn = genAllCombHelper(@toReturn);
}
return @toReturn;
}


#input: one design string
#output: one pigeon formatted design string
sub convertToPigon {

}


$inputFile= $ARGV[0];
open(INPUT, "$inputFile") || die "Sorry, could not open $!";

#Each of the following arrays are indexed in the dame fashion
my @expressions; #array that will hold all of the expressions read in
my @designs; #array that will hold all of the designs read in
my @truthTables; #array that will hold all of the truth tables read in

my $counter = 0;
while (<INPUT>) {
chomp;
	if ($counter == 0) {
		push(@expressions,$_);
	} elsif ($counter == 1) {
		push(@designs,$_);
	} elsif ($counter == 2) {
		push(@truthTables,$_);
	}	
	if($counter == 2) {
		$counter = 0;
	} else {
		$counter++;
	}
}
close INPUT;
open (OUTPUT, ">".$ARGV[1]);

for(my $i=0; $i<@expressions; $i++) {
	print(OUTPUT $expressions[$i]."\n");
	print(OUTPUT $designs[$i]."\n");	
	my @results = translateTransToRecombdesign($designs[$i]);
	for(my $j=0; $j<@results; $j++) {
		print(OUTPUT $results[$j]."\n");
	}
	print (OUTPUT $truthTables[$i]."\n");	
}
close (OUTPUT);
















