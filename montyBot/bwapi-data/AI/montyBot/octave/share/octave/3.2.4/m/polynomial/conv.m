## Copyright (C) 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2002, 2004,
##               2005, 2006, 2007, 2008 John W. Eaton
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
## @deftypefn {Function File} {} conv (@var{a}, @var{b})
## Convolve two vectors.
##
## @code{y = conv (a, b)} returns a vector of length equal to
## @code{length (a) + length (b) - 1}.
## If @var{a} and @var{b} are polynomial coefficient vectors, @code{conv}
## returns the coefficients of the product polynomial.
## @seealso{deconv, poly, roots, residue, polyval, polyderiv, polyinteg}
## @end deftypefn

## Author: Tony Richardson <arichard@stark.cc.oh.us>
## Created: June 1994
## Adapted-By: jwe

function y = conv (a, b)

  if (nargin != 2)
    print_usage ();
  endif

  if (! (isvector (a) && isvector (b)))
    error("conv: both arguments must be vectors");
  endif

  la = length (a);
  lb = length (b);

  ly = la + lb - 1;

  ## Use the shortest vector as the coefficent vector to filter.
  ## Preserve the row/column orientation of the longer input.
  if (la < lb)
    if (ly > lb)
      if (size (b, 1) <= size (b, 2))
        x = [b, (zeros (1, ly - lb))];
      else
        x = [b; (zeros (ly - lb, 1))];
      endif
    else
      x = b;
    endif
    y = filter (a, 1, x);
  else
    if(ly > la)
      if (size (a, 1) <= size (a, 2))
        x = [a, (zeros (1, ly - la))];
      else
        x = [a; (zeros (ly - la, 1))];
      endif
    else
      x = a;
    endif
    y = filter (b, 1, x);
  endif

endfunction

%!assert(all (all (conv (ones (3, 1), ones (3, 1)) == [1; 2; 3; 2; 1])));

%!assert(all (all (conv (ones (1, 3), ones (3, 1)) == [1, 2, 3, 2, 1])));

%!assert(all (all (conv (3, [1, 2, 3]) == [3, 6, 9])));

%!error conv ([1, 2; 3, 4], 3);

%!assert(conv (2, 3),6);

%!error conv (2, []);

%!test
%! a = 1:10;
%! b = 1:3;
%! c = conv (a, b);
%! assert (size(c), [1, numel(a)+numel(b)-1])
%!test
%! a = (1:10).';
%! b = 1:3;
%! c = conv (a, b);
%! assert (size(c), [numel(a)+numel(b)-1, 1])
%!test
%! a = 1:10;
%! b = (1:3).';
%! c = conv (a, b);
%! assert (size(c), [1, numel(a)+numel(b)-1])

%!test
%! b = 1:10;
%! a = 1:3;
%! c = conv (a, b);
%! assert (size(c), [1, numel(a)+numel(b)-1])
%!test
%! b = (1:10).';
%! a = 1:3;
%! c = conv (a, b);
%! assert (size(c), [numel(a)+numel(b)-1, 1])
%!test
%! b = 1:10;
%! a = (1:3).';
%! c = conv (a, b);
%! assert (size(c), [1, numel(a)+numel(b)-1])

