## Copyright (C) 1995, 1996, 1999, 2000, 2002, 2004, 2005, 2006, 2007, 2008
##               Kurt Hornik
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
## @deftypefn {Function File} {} vec (@var{x})
## Return the vector obtained by stacking the columns of the matrix @var{x}
## one above the other.
## @end deftypefn

## See Magnus and Neudecker (1988), Matrix differential calculus with
## applications in statistics and econometrics.

## Author: KH <Kurt.Hornik@wu-wien.ac.at>
## Created: 8 May 1995
## Adapted-By: jwe

function v = vec (x)

  if (nargin != 1)
    print_usage ();
  endif

  v = x(:);

endfunction

%!assert(vec ([1, 2; 3, 4]) == [1; 3; 2; 4] && vec ([1, 3, 2, 4]) == [1; 3; 2; 4]);

%!error vec ();

%!error vec (1, 2);

