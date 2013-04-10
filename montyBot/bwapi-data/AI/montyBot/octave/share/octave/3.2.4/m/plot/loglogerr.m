## Copyright (C) 2000, 2001, 2002, 2004, 2005, 2006, 2007, 2009
##               Teemu Ikonen
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
## @deftypefn {Function File} {} loglogerr (@var{args})
## Produce two-dimensional plots on double logarithm axis with
## errorbars.  Many different combinations of arguments are possible.
## The most used form is
##
## @example
## loglogerr (@var{x}, @var{y}, @var{ey}, @var{fmt})
## @end example
##
## @noindent
## which produces a double logarithm plot of @var{y} versus @var{x} 
## with errors in the @var{y}-scale defined by @var{ey} and the plot
## format defined by @var{fmt}.  See errorbar for available formats and 
## additional information.
## @seealso{errorbar, semilogxerr, semilogyerr}
## @end deftypefn

## Created: 20.2.2001
## Author: Teemu Ikonen <tpikonen@pcu.helsinki.fi>
## Keywords: errorbar, plotting

function retval = loglogerr (varargin)

  [h, varargin] = __plt_get_axis_arg__ ("loglogerr", varargin{:});

  oldh = gca ();
  unwind_protect
    axes (h);
    newplot ();

    set (h, "xscale", "log", "yscale", "log");

    tmp = __errcomm__ ("loglogerr", h, varargin{:});

    if (nargout > 0)
      retval = tmp;
    endif
  unwind_protect_cleanup
    axes (oldh);
  end_unwind_protect

endfunction
