## Copyright (C) 1995, 1998, 2000, 2002, 2004, 2005, 2006, 2007, 2008, 2009
##               Auburn University.  All rights reserved.
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
## @deftypefn {Function File} {} polyout (@var{c}, @var{x})
## Write formatted polynomial
## @tex
## $$ c(x) = c_1 x^n + \ldots + c_n x + c_{n+1} $$
## @end tex
## @ifnottex
## @example
##    c(x) = c(1) * x^n + @dots{} + c(n) x + c(n+1)
## @end example
## @end ifnottex
##  and return it as a string or write it to the screen (if
##  @var{nargout} is zero).
##  @var{x} defaults to the string @code{"s"}.
## @seealso{polyval, polyvalm, poly, roots, conv, deconv, residue,
## filter, polyderiv, polyinteg}
## @end deftypefn

## Author: A. S. Hodel <a.s.hodel@eng.auburn.edu>
## Created: May 1995
## Nov 1998: Correctly handles complex coefficients

function y = polyout (c, x)

  if (nargin < 1) || (nargin > 2) || (nargout < 0) || (nargout > 1)
    print_usage ();
  endif

  if (! isvector (c))
    error ("polyout: first argument must be a vector");
  endif

  if (nargin == 1)
    x = "s";
  elseif (! ischar (x))
    error ("polyout: second argument must be a string");
  endif

  n = length (c);
  if(n > 0)
    n1 = n+1;

    tmp = coeff (c(1));
    for ii = 2:n
      if (real (c(ii)) < 0)
	ns = " - ";
	c(ii) = -c(ii);
      else
        ns = " + ";
      endif

      tmp = sprintf ("%s*%s^%d%s%s", tmp, x, n1-ii, ns, coeff (c(ii)));

    endfor
  else
    tmp = " ";
  endif

  if(nargout == 0)
    disp (tmp)
  else
    y = tmp;
  endif

endfunction

function str = coeff (c)
  if (imag (c))
    if (real (c))
      str = sprintf ("(%s)", num2str (c, 5));
    else
      str = num2str (c, 5);
    endif
  else
    str = num2str (c, 5);
  endif
endfunction
