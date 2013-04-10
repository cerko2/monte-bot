## Copyright (C) 1994, 1995, 1996, 1997, 1999, 2005, 2007, 2008,
##               2009 John W. Eaton
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
## @deftypefn {Function File} {} version ()
## Return Octave's version number as a string.  This is also the value of
## the built-in variable @w{@code{OCTAVE_VERSION}}.
## @end deftypefn

## Author: jwe

function vs = version ()

  if (nargin != 0)
    warning ("version: ignoring extra arguments");
  endif

  vs = OCTAVE_VERSION;

endfunction

%!assert(ischar (version ()) && strcmp (version (), OCTAVE_VERSION));

%!warning version (1);

