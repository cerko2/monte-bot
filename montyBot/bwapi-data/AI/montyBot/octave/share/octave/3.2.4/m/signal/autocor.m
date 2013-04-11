## Copyright (C) 1995, 1996, 1997, 1998, 2000, 2002, 2005, 2006, 2007
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
## @deftypefn {Function File} {} autocor (@var{x}, @var{h})
## Return the autocorrelations from lag 0 to @var{h} of vector @var{x}.
## If @var{h} is omitted, all autocorrelations are computed.
## If @var{x} is a matrix, the autocorrelations of each column are
## computed.
## @end deftypefn

## Author: FL <Friedrich.Leisch@ci.tuwien.ac.at>
## Description: Compute autocorrelations

function retval = autocor (X, h)

  if (nargin == 1)
    retval = autocov (X);
  elseif (nargin == 2)
    retval = autocov (X, h);
  else
    print_usage ();
  endif

  if (min (retval (1,:)) != 0)
    retval = retval ./ (ones (rows (retval), 1) * retval(1,:));
  endif

endfunction



