## Copyright (C) 1995, 1996, 1997, 2005, 2006, 2007 Kurt Hornik
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
## @deftypefn {Function File} {} finv (@var{x}, @var{m}, @var{n})
## For each component of @var{x}, compute the quantile (the inverse of
## the CDF) at @var{x} of the F distribution with parameters @var{m} and
## @var{n}.
## @end deftypefn

## Author: KH <Kurt.Hornik@wu-wien.ac.at>
## Description: Quantile function of the F distribution

function inv = finv (x, m, n)

  if (nargin != 3)
    print_usage ();
  endif

  if (!isscalar (m) || !isscalar (n))
    [retval, x, m, n] = common_size (x, m, n);
    if (retval > 0)
      error ("finv: x, m and n must be of common size or scalar");
    endif
  endif

  sz = size (x);
  inv = zeros (sz);

  k = find ((x < 0) | (x > 1) | isnan (x) | !(m > 0) | !(n > 0));
  if (any (k))
    inv(k) = NaN;
  endif

  k = find ((x == 1) & (m > 0) & (n > 0));
  if (any (k))
    inv(k) = Inf;
  endif

  k = find ((x > 0) & (x < 1) & (m > 0) & (n > 0));
  if (any (k))
    if (isscalar (m) && isscalar (n))
      inv(k) = ((1 ./ betainv (1 - x(k), n / 2, m / 2) - 1) .* n ./ m);
    else
      inv(k) = ((1 ./ betainv (1 - x(k), n(k) / 2, m(k) / 2) - 1)
		.* n(k) ./ m(k));
    endif
  endif

endfunction
