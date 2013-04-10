## Copyright (C) 1995, 1996, 1997, 1998, 1999, 2000, 2005, 2007, 2008
##               John W. Eaton
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
## @deftypefn {Function File} {} date ()
## Return the date as a character string in the form DD-MMM-YY.  For
## example,
##
## @example
## @group
## date ()
##      @result{} "20-Aug-93"
## @end group
## @end example
## @end deftypefn

## Author: jwe

function retval = date ()

  retval = strftime ("%d-%b-%Y", localtime (time ()));

endfunction

%!assert(strcmp (date (), strftime ("%d-%b-%Y", localtime (time ()))));

