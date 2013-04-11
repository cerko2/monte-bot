## Copyright (C) 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2005, 2006,
##               2007 John W. Eaton
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
## @deftypefn {Function File} {} betai (@var{a}, @var{b}, @var{x})
## This function is provided for compatibility with older versions of
## Octave.  New programs should use betainc instead.
##
## @code{betai (@var{a}, @var{b}, @var{x})} is the same as @code{betainc
## (@var{x}, @var{a}, @var{b})}. 
## @end deftypefn

## Author: jwe
## Created: 30 Jan 1998

function retval = betai (a, b, x)

  if (nargin == 3)
    retval = betainc (x, a, b);
  else
    print_usage ();
  endif

endfunction
