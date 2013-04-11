## Copyright (C) 1996, 1997, 1999, 2002, 2003, 2005, 2006, 2007, 2008, 2009
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
## @deftypefn {Function File} {} blanks (@var{n})
## Return a string of @var{n} blanks, for example:
##
## @example
## @group
## blanks(10);
## whos ans;
##      @result{}
##       Attr Name        Size                     Bytes  Class
##       ==== ====        ====                     =====  ===== 
##            ans         1x10                        10  char
## @end group
## @end example
## @seealso{repmat}
## @end deftypefn

## Author: Kurt Hornik <Kurt.Hornik@wu-wien.ac.at>
## Adapted-By: jwe

function s = blanks (n)

  if (nargin != 1)
    print_usage ();
  elseif (! (isscalar (n) && n == round (n)))
    error ("blanks: n must be a non-negative integer");
  endif

  ## If 1:n is empty, the following expression will create an empty
  ## character string.  Otherwise, it will create a row vector.
  s(1:n) = " ";

endfunction

## There really isn't that much to test here
%!assert(blanks (0), "")
%!assert(blanks (5), "     ")
%!assert(blanks (10), "          ")

%!assert(strcmp (blanks (3), "   "));

%!error blanks ();

%!error blanks (1, 2);

