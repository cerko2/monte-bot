## Copyright (C) 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2002, 2003,
##               2005, 2006, 2007, 2008, 2009 John W. Eaton
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
## @deftypefn {Function File} {} cstrcat (@var{s1}, @var{s2}, @dots{})
## Return a string containing all the arguments concatenated
## horizontally.  Trailing white space is preserved.  For example,
##
## @example
## @group
## cstrcat ("ab   ", "cd")
##      @result{} "ab   cd"
## @end group
## @end example
##
## @example
## @group
## s = [ "ab"; "cde" ];
## cstrcat (s, s, s)
##      @result{} ans =
##         "ab ab ab "
##         "cdecdecde"
## @end group
## @end example
## @seealso{strcat, char, strvcat}
## @end deftypefn

## Author: jwe

function st = cstrcat (varargin)

  if (nargin > 0)

    if (iscellstr (varargin))
      ## All arguments are character strings.
      unwind_protect
	tmp = warning ("query", "Octave:empty-list-elements");
	warning ("off", "Octave:empty-list-elements");
	st = [varargin{:}];
      unwind_protect_cleanup
	warning (tmp.state, "Octave:empty-list-elements");
      end_unwind_protect
    else
      error ("cstrcat: expecting arguments to character strings");
    endif
  else
    print_usage ();
  endif

endfunction

## test the dimensionality
## 1d
%!assert(cstrcat("ab ", "ab "), "ab ab ")
## 2d
%!assert(cstrcat(["ab ";"cde"], ["ab ";"cde"]), ["ab ab ";"cdecde"])

%!assert((strcmp (cstrcat ("foo", "bar"), "foobar")
%! && strcmp (cstrcat (["a"; "bb"], ["foo"; "bar"]), ["a foo"; "bbbar"])));

%!error cstrcat ();

%!error cstrcat (1, 2);

