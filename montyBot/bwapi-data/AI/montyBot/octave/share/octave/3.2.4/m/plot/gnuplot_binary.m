## Copyright (C) 2008 John W. Eaton
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
## @deftypefn {Loadable Function} {@var{val} =} gnuplot_binary ()
## @deftypefnx {Loadable Function} {@var{old_val} =} gnuplot_binary (@var{new_val})
## Query or set the name of the program invoked by the plot command.
## The default value @code{\"gnuplot\"}.  @xref{Installation}.
## @end deftypefn

## Author: jwe

function retval = gnuplot_binary (new_val)

  persistent gp_binary = "gnuplot";

  if (nargout > 0 || nargin == 0)
    retval = gp_binary;
  endif

  if (nargin == 1)
    if (ischar (new_val))
      if (! isempty (new_val))
	gp_binary = new_val;
      else
	error ("gnuplot_binary: value must not be empty");
      endif
    else
      error ("gnuplot_binary: expecting arg to be a character string");
    endif
  elseif (nargin > 1)
    print_usage ();
  endif

endfunction
