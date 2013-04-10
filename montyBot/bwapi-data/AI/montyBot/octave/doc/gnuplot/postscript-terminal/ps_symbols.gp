set size 0.67,1
set terminal postscript lw 1.5; set output 'ps_symbols.ps'
set title 'Symbols and linetypes in Postscript terminal'
set noborder; set nozeroaxis; set noxtics; set noytics
set xlabel ''; set ylabel ''; set nokey; set size square;
set xrange [-1.2:1.2]; set yrange [-1.2:1.2]; set parametric; set samples 2
f(x)=cos(pi*x/38.0); g(x)=sin(pi*x/38.0)
set label '(#70--75 are opaque)' at 0,0 center
set label  '0' at  1.07*f(0),  1.07*g(0) center
set label  '5' at  1.07*f(5),  1.07*g(5) center
set label '10' at  1.07*f(10), 1.07*g(10) center
set label '15' at  1.07*f(15), 1.07*g(15) center
set label '20' at  1.07*f(20), 1.07*g(20) center
set label '25' at  1.07*f(25), 1.07*g(25) center
set label '30' at  1.07*f(30), 1.07*g(30) center
set label '35' at  1.07*f(35), 1.07*g(35) center
set label '40' at  1.07*f(40), 1.07*g(40) center
set label '45' at  1.07*f(45), 1.07*g(45) center
set label '50' at  1.07*f(50), 1.07*g(50) center
set label '55' at  1.07*f(55), 1.07*g(55) center
set label '60' at  1.07*f(60), 1.07*g(60) center
set label '65' at  1.07*f(65), 1.07*g(65) center
set label '70' at  1.07*f(70), 1.07*g(70) center
set label '75' at  1.07*f(75), 1.07*g(75) center

unset arrow
f1(x)=0.45 *cos(pi*x/38.0); g1(x)=0.45 *sin(pi*x/38.0)
f2(x)=0.97*cos(pi*x/38.0); g2(x)=0.97*sin(pi*x/38.0)
l= 0; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l
l=l+1; set arrow from f1(l),g1(l) to f2(l),g2(l) nohead lt l

plot f( 0),g( 0) w p lt 0 pt  0,\
 f( 1),g( 1) w p lt 0 pt  1,  f( 2),g( 2) w p lt 0 pt  2,  f( 3),g( 3) w p lt 0 pt  3,\
 f( 4),g( 4) w p lt 0 pt  4,  f( 5),g( 5) w p lt 0 pt  5,  f( 6),g( 6) w p lt 0 pt  6,\
 f( 7),g( 7) w p lt 0 pt  7,  f( 8),g( 8) w p lt 0 pt  8,  f( 9),g( 9) w p lt 0 pt  9,\
 f(10),g(10) w p lt 0 pt 10,  f(11),g(11) w p lt 0 pt 11,  f(12),g(12) w p lt 0 pt 12,\
 f(13),g(13) w p lt 0 pt 13,  f(14),g(14) w p lt 0 pt 14,  f(15),g(15) w p lt 0 pt 15,\
 f(16),g(16) w p lt 0 pt 16,  f(17),g(17) w p lt 0 pt 17,  f(18),g(18) w p lt 0 pt 18,\
 f(19),g(19) w p lt 0 pt 19,  f(20),g(20) w p lt 0 pt 20,  f(21),g(21) w p lt 0 pt 21,\
 f(22),g(22) w p lt 0 pt 22,  f(23),g(23) w p lt 0 pt 23,  f(24),g(24) w p lt 0 pt 24,\
 f(25),g(25) w p lt 0 pt 25,  f(26),g(26) w p lt 0 pt 26,  f(27),g(27) w p lt 0 pt 27,\
 f(28),g(28) w p lt 0 pt 28,  f(29),g(29) w p lt 0 pt 29,  f(30),g(30) w p lt 0 pt 30,\
 f(31),g(31) w p lt 0 pt 31,  f(32),g(32) w p lt 0 pt 32,  f(33),g(33) w p lt 0 pt 33,\
 f(34),g(34) w p lt 0 pt 34,  f(35),g(35) w p lt 0 pt 35,  f(36),g(36) w p lt 0 pt 36,\
 f(37),g(37) w p lt 0 pt 37,  f(38),g(38) w p lt 0 pt 38,  f(39),g(39) w p lt 0 pt 39,\
 f(40),g(40) w p lt 0 pt 40,  f(41),g(41) w p lt 0 pt 41,  f(42),g(42) w p lt 0 pt 42,\
 f(43),g(43) w p lt 0 pt 43,  f(44),g(44) w p lt 0 pt 44,  f(45),g(45) w p lt 0 pt 45,\
 f(46),g(46) w p lt 0 pt 46,  f(47),g(47) w p lt 0 pt 47,  f(48),g(48) w p lt 0 pt 48,\
 f(49),g(49) w p lt 0 pt 49,  f(50),g(50) w p lt 0 pt 50,  f(51),g(51) w p lt 0 pt 51,\
 f(52),g(52) w p lt 0 pt 52,  f(53),g(53) w p lt 0 pt 53,  f(54),g(54) w p lt 0 pt 54,\
 f(55),g(55) w p lt 0 pt 55,  f(56),g(56) w p lt 0 pt 56,  f(57),g(57) w p lt 0 pt 57,\
 f(58),g(58) w p lt 0 pt 58,  f(59),g(59) w p lt 0 pt 59,  f(60),g(60) w p lt 0 pt 60,\
 f(61),g(61) w p lt 0 pt 61,  f(62),g(62) w p lt 0 pt 62,  f(63),g(63) w p lt 0 pt 63,\
 f(64),g(64) w p lt 0 pt 64,  f(65),g(65) w p lt 0 pt 65,  f(66),g(66) w p lt 0 pt 66,\
 f(67),g(67) w p lt 0 pt 67,  f(68),g(68) w p lt 0 pt 68,  f(69),g(69) w p lt 0 pt 69,\
 f(70),g(70) w p lt 0 pt 70,  f(71),g(71) w p lt 0 pt 71,  f(72),g(72) w p lt 0 pt 72,\
 f(73),g(73) w p lt 0 pt 73,  f(74),g(74) w p lt 0 pt 74,  f(75),g(75) w p lt 0 pt 75

set output
