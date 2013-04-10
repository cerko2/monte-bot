## Copyright (C) 2000, 2005, 2006, 2007, 2008, 2009 Paul Kienzle
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
## @deftypefn {Function File} {@var{q} =} polygcd (@var{b}, @var{a}, @var{tol})
##
## Find greatest common divisor of two polynomials.  This is equivalent
## to the polynomial found by multiplying together all the common roots.
## Together with deconv, you can reduce a ratio of two polynomials.
## Tolerance defaults to 
## @example 
## sqrt(eps).
## @end example
##  Note that this is an unstable
## algorithm, so don't try it on large polynomials.
##
## Example
## @example
## @group
## polygcd (poly(1:8), poly(3:12)) - poly(3:8)
## @result{} [ 0, 0, 0, 0, 0, 0, 0 ]
## deconv (poly(1:8), polygcd (poly(1:8), poly(3:12))) ...
##   - poly(1:2)
## @result{} [ 0, 0, 0 ]
## @end group
## @end example
## @seealso{poly, polyinteg, polyderiv, polyreduce, roots, conv, deconv,
## residue, filter, polyval, polyvalm}
## @end deftypefn

function x = polygcd (b, a, tol)

  if (nargin == 2 || nargin == 3)
    if (nargin == 2)
      if (isa (a, "single") || isa (b, "single"))
	tol = sqrt (eps ("single"));
      else
	tol = sqrt (eps);
      endif
    endif
    if (length (a) == 1 || length (b) == 1)
      if (a == 0)
	x = b;
      elseif (b == 0)
	x = a;
      else
	x = 1;
      endif
    else
      a /= a(1);
      while (1)
	[d, r] = deconv (b, a);
	nz = find (abs (r) > tol);
	if (isempty (nz))
	  x = a;
	  break;
	else
	  r = r(nz(1):length(r));
	endif
	b = a;
	a = r / r(1);
      endwhile
    endif
  else
    print_usage ();
  endif

endfunction
