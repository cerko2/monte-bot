## Copyright (C) 1995, 1996, 1997, 1998, 2000, 2002, 2004, 2005, 2006,
##               2007, 2009 Kurt Hornik
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
## @deftypefn {Function File} {} center (@var{x})
## @deftypefnx {Function File} {} center (@var{x}, @var{dim})
## If @var{x} is a vector, subtract its mean.
## If @var{x} is a matrix, do the above for each column.
## If the optional argument @var{dim} is given, perform the above
## operation along this dimension
## @end deftypefn

## Author: KH <Kurt.Hornik@wu-wien.ac.at>
## Description: Center by subtracting means

function retval = center (x, dim)

  if (nargin != 1 && nargin != 2)
    print_usage ();
  endif

  if (nargin < 2)
    t = find (size (x) != 1);
    if (isempty (t))
      dim = 1;
    else
      dim = t(1);
    endif
  endif
  n = size (x, dim);

  if (n == 1)
    retval = zeros (size (x));
  elseif (n > 0)
    if (isvector (x))
      retval = x - sum (x) / n;
    else
      mx = sum (x, dim) / n;
      idx(1:ndims (x)) = {':'}; 
      idx{dim} = ones (1, n);
      retval = x - mx(idx{:});
    endif
  else
    retval = x;
  endif
endfunction
