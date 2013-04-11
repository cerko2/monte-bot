## Copyright (C) 1993, 1994, 1995, 1996, 1997, 1999, 2000, 2004, 2005,
##               2006, 2007, 2008 John W. Eaton
##
## This file is part of Octave.
##
## Octave is free software; you can redistribute it and/or modify it
## under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or (at
## your option) any later version.
##
## Octave is distributed in the hope that it will be useful, but
## WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
## General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with Octave; see the file COPYING.  If not, see
## <http://www.gnu.org/licenses/>.

## -*- texinfo -*-
## @deftypefn {Function File} {} tril (@var{a}, @var{k})
## @deftypefnx {Function File} {} triu (@var{a}, @var{k})
## Return a new matrix formed by extracting the lower (@code{tril})
## or upper (@code{triu}) triangular part of the matrix @var{a}, and
## setting all other elements to zero.  The second argument is optional,
## and specifies how many diagonals above or below the main diagonal should
## also be set to zero.
##
## The default value of @var{k} is zero, so that @code{triu} and
## @code{tril} normally include the main diagonal as part of the result
## matrix.
##
## If the value of @var{k} is negative, additional elements above (for
## @code{tril}) or below (for @code{triu}) the main diagonal are also
## selected.
##
## The absolute value of @var{k} must not be greater than the number of
## sub- or super-diagonals.
##
## For example,
##
## @example
## @group
## tril (ones (3), -1)
##      @result{}  0  0  0
##          1  0  0
##          1  1  0
## @end group
## @end example
##
## @noindent
## and
##
## @example
## @group
## tril (ones (3), 1)
##      @result{}  1  1  0
##          1  1  1
##          1  1  1
## @end group
## @end example
## @seealso{triu, diag}
## @end deftypefn

## Author: jwe

function retval = tril (x, k)

  if (nargin > 0)
    if (isstruct (x))
       error ("tril: structure arrays not supported");
     endif 
    [nr, nc] = size (x);
  endif

  if (nargin == 1)
    k = 0;
  elseif (nargin == 2)
    if ((k > 0 && k > nc) || (k < 0 && k < -nr))
      error ("tril: requested diagonal out of range");
    endif
  else
    print_usage ();
  endif

  retval = resize (resize (x, 0), nr, nc);
  for j = 1 : min (nc, nr+k)
    nr_limit = max (1, j-k);
    retval (nr_limit:nr, j) = x (nr_limit:nr, j);
  endfor

endfunction

%!test
%! a = [1, 2, 3; 4, 5, 6; 7, 8, 9; 10, 11, 12];
%! 
%! l0 = [1, 0, 0; 4, 5, 0; 7, 8, 9; 10, 11, 12];
%! l1 = [1, 2, 0; 4, 5, 6; 7, 8, 9; 10, 11, 12];
%! l2 = [1, 2, 3; 4, 5, 6; 7, 8, 9; 10, 11, 12];
%! lm1 = [0, 0, 0; 4, 0, 0; 7, 8, 0; 10, 11, 12];
%! lm2 = [0, 0, 0; 0, 0, 0; 7, 0, 0; 10, 11, 0];
%! lm3 = [0, 0, 0; 0, 0, 0; 0, 0, 0; 10, 0, 0];
%! lm4 = [0, 0, 0; 0, 0, 0; 0, 0, 0; 0, 0, 0];
%! 
%! assert((tril (a, -4) == lm4 && tril (a, -3) == lm3
%! && tril (a, -2) == lm2 && tril (a, -1) == lm1
%! && tril (a) == l0 && tril (a, 1) == l1 && tril (a, 2) == l2));

%!error tril ();

%!error tril (1, 2, 3);

