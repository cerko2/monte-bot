## Copyright (C) 1993, 1994, 1995, 1996, 1997, 1999, 2000, 2005, 2006,
##               2007, 2008 John W. Eaton
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
## @deftypefn {Function File} {} triu (@var{a}, @var{k})
## See tril.
## @end deftypefn

## Author: jwe

function retval = triu (x, k)

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
      error ("triu: requested diagonal out of range");
    endif
  else
    print_usage ();
  endif

  retval = resize (resize (x, 0), nr, nc);
  for j = max (1, k+1) : nc
    nr_limit = min (nr, j-k);
    retval (1:nr_limit, j) = x (1:nr_limit, j);
  endfor

endfunction

%!test
%! a = [1, 2, 3; 4, 5, 6; 7, 8, 9; 10, 11, 12];
%! 
%! u0 = [1, 2, 3; 0, 5, 6; 0, 0, 9; 0, 0, 0];
%! u1 = [0, 2, 3; 0, 0, 6; 0, 0, 0; 0, 0, 0];
%! u2 = [0, 0, 3; 0, 0, 0; 0, 0, 0; 0, 0, 0];
%! u3 = [0, 0, 0; 0, 0, 0; 0, 0, 0; 0, 0, 0];
%! um1 = [1, 2, 3; 4, 5, 6; 0, 8, 9; 0, 0, 12];
%! um2 = [1, 2, 3; 4, 5, 6; 7, 8, 9; 0, 11, 12];
%! um3 = [1, 2, 3; 4, 5, 6; 7, 8, 9; 10, 11, 12];
%! 
%! assert((triu (a, -3) == um3 && triu (a, -2) == um2
%! && triu (a, -1) == um1 && triu (a) == u0 && triu (a, 1) == u1
%! && triu (a, 2) == u2 && triu (a, 3) == u3));

%!error triu ();

%!error triu (1, 2, 3);

