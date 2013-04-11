## Copyright (C) 2000, 2005, 2006, 2007, 2009 Paul Kienzle
## Copyright (C) 2003 Alois Schloegl
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
## @deftypefn {Function File} {} strmatch (@var{s}, @var{a}, "exact")
## Return indices of entries of @var{a} that match the string @var{s}.
## The second argument @var{a} may be a string matrix or a cell array of
## strings.  If the third argument @code{"exact"} is not given, then
## @var{s} only needs to match @var{a} up to the length of @var{s}.  Nul
## characters match blanks.  Results are returned as a column vector. 
## For example:
##
## @example
## @group
## strmatch ("apple", "apple juice")
##      @result{} 1
##
## strmatch ("apple", ["apple pie"; "apple juice"; "an apple"])
##      @result{} [1; 2]
##
## strmatch ("apple", @{"apple pie"; "apple juice"; "tomato"@})
##      @result{} [1; 2]
## @end group
## @end example
## @seealso{strfind, findstr, strcmp, strncmp, strcmpi, strncmpi, find}
## @end deftypefn

## Author: Paul Kienzle, Alois Schloegl
## Adapted-by: jwe

function idx = strmatch (s, A, exact)

  if (nargin < 2 || nargin > 3)
    print_usage ();
  endif

  [nr, nc] = size (A);
  nel = numel (A);
  if (iscell (A))
    match = zeros (nel, 1);
    if (nargin > 2)
      for k = 1:nel
	match(k) = strcmp (s, A{k}); 
      endfor
    else
      for k = 1:nel
	match(k) = strncmp (s, A{k}, length (s));
      endfor
    endif
    idx = find (match);
  elseif (length (s) > nc)
    idx = [];
  else
    if (nargin == 3 && length(s) < nc)
      s(1,nc) = " ";
    endif
    s(s == 0) = " ";
    A(A == 0) = " ";
    match = s(ones(size(A,1),1),:) == A(:,1:length(s));
    if (length (s) == 1)
      idx = find (match);
    else
      idx = find (all (match')');
    endif
  endif
    
endfunction 

%!error <Invalid call to strmatch> strmatch();
%!error <Invalid call to strmatch> strmatch("a", "aaa", "exact", 1);
%!assert (strmatch("a", {"aaa", "bab", "bbb"}), 1);
%!assert (strmatch ("apple", "apple juice"), 1);
%!assert (strmatch ("apple", ["apple pie"; "apple juice"; "an apple"]),
%!        [1; 2]);
%!assert (strmatch ("apple", {"apple pie"; "apple juice"; "tomato"}),
%!        [1; 2]);
%!assert (strmatch ("apple pie", "apple"), []);
