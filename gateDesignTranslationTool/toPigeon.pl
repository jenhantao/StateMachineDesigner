#! /usr/bin/perl -w

@tokens = split(" ", $ARGV[0]);
open(OUTPUT, ">".$ARGV[1]);
for(my $i=0; $i<@tokens; $i++) {
$tokens[$i] =~ s/>\(/> \(/g;
$tokens[$i] =~ s/<\(/< \(/g;
$tokens[$i] =~ s/g/c g/g;
$tokens[$i] =~ s/p/p p/g;
print OUTPUT $tokens[$i]."\n";
}
print OUTPUT "# Arcs";
close(OUTPUT);
