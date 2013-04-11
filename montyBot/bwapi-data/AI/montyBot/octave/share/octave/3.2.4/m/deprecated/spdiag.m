## Copyright (C) 2008, 2009 David Bateman
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
## @deftypefn {Function File} {} spdiag (@var{v}, @var{k})
## This function has been deprecated.  Use @code{sparse (diag (@dots{}))} instead.
## @end deftypefn

## Deprecated in version 3.2

function retval = spdiag (varargin)
  persistent warned = false;
  if (! warned)
    warned = true;
    warning ("Octave:deprecated-function",
	     "spdiag is obsolete and will be removed from a future version of Octave; please use sparse (diag (...)) instead");
  endif

  retval = sparse (diag (varargin{:}));

endfunction
