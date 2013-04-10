## Copyright (C) 1999, 2000, 2007, 2009 Kai Habel
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
## @deftypefn {Function File} {} hsv (@var{n})
## Create color colormap.  This colormap is red through yellow, green,
## cyan, blue, magenta to red.  It is obtained by linearly varying the
## hue through all possible values while keeping constant maximum
## saturation and value and is equivalent to
## @code{hsv2rgb ([linspace(0,1,N)', ones(N,2)])}.
##
## The argument @var{n} should be a scalar.  If it is omitted, the
## length of the current colormap or 64 is assumed.
## @seealso{colormap}
## @end deftypefn

## Author:  Kai Habel <kai.habel@gmx.de>

function map = hsv (number)

  if (nargin == 0)
    number = rows (colormap);
  elseif (nargin == 1)
    if (! isscalar (number))
      error ("hsv: argument must be a scalar");
    endif
  else
    print_usage ();
  endif

  if (number == 1)
    map = [1, 0, 0];  
  elseif (number > 1)
    h = linspace (0, 1, number)';
    map = hsv2rgb ([h, ones(number, 1), ones(number, 1)]);
  else
    map = [];
  endif

endfunction
