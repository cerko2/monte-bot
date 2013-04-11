## Copyright (C) 1995, 1996, 1997, 2005, 2006, 2007, 2008 Kurt Hornik
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
## @deftypefn {Function File} {} expcdf (@var{x}, @var{lambda})
## For each element of @var{x}, compute the cumulative distribution
## function (CDF) at @var{x} of the exponential distribution with
## mean @var{lambda}.
##
## The arguments can be of common size or scalar.
## @end deftypefn

## Author: KH <Kurt.Hornik@wu-wien.ac.at>
## Description: CDF of the exponential distribution

function cdf = expcdf (x, l)

  if (nargin != 2)
    print_usage ();
  endif

  if (!isscalar (x) && !isscalar(l))
    [retval, x, l] = common_size (x, l);
    if (retval > 0)
      error ("expcdf: x and lambda must be of common size or scalar");
    endif
  endif

  if (isscalar (x))
    sz = size (l);
  else
    sz = size (x);
  endif

  cdf = zeros (sz);

  k = find (isnan (x) | !(l > 0));
  if (any (k))
    cdf(k) = NaN;
  endif

  k = find ((x == Inf) & (l > 0));
  if (any (k))
    cdf(k) = 1;
  endif

  k = find ((x > 0) & (x < Inf) & (l > 0));
  if (any (k))
    if isscalar (l)
      cdf (k) = 1 - exp (- x(k) ./ l);
    elseif isscalar (x)
      cdf (k) = 1 - exp (- x ./ l(k));
    else
      cdf (k) = 1 - exp (- x(k) ./ l(k));
    endif
  endif

endfunction
