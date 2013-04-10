## Copyright (C) 1996, 1997, 2002, 2004, 2005, 2006, 2007, 2008 John W. Eaton
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
## @deftypefn {Function File} {} isscalar (@var{a})
## Return 1 if @var{a} is a scalar.  Otherwise, return 0.
## @seealso{size, rows, columns, length, isscalar, ismatrix}
## @end deftypefn

## Author: jwe

function retval = isscalar (x)

  if (nargin == 1)
    retval = numel (x) == 1;
  else
    print_usage ();
  endif

endfunction

%!assert(isscalar (1));

%!assert(!(isscalar ([1, 2])));

%!assert(!(isscalar ([])));

%!assert(!(isscalar ([1, 2; 3, 4])));

%!test
%! warn_str_to_num = 0;
%! assert((isscalar ("t")));

%!assert(!(isscalar ("test")));

%!assert(!(isscalar (["test"; "ing"])));

%!test
%! s.a = 1;
%! assert((isscalar (s)));

%!error isscalar ();

%!error isscalar (1, 2);

