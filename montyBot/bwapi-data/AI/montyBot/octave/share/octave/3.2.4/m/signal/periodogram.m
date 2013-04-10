## Copyright (C) 1995, 1996, 1997, 1998, 1999, 2000, 2002, 2005, 2007
##               Friedrich Leisch
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
## @deftypefn {Function File} {} periodogram (@var{x})
## For a data matrix @var{x} from a sample of size @var{n}, return the
## periodogram.
## @end deftypefn

## Author: FL <Friedrich.Leisch@ci.tuwien.ac.at>
## Description: Compute the periodogram

function retval = periodogram (x)

  if (nargin != 1)
    print_usage ();
  endif

  [r, c] = size(x);

  if (r == 1)
    r = c;
  endif

  retval = (abs (fft (x - mean (x)))) .^ 2 / r;

endfunction







