## Copyright (C) 1993, 1994, 1995, 1996, 1997, 1999, 2000, 2002, 2003,
##               2004, 2005, 2006, 2007, 2008, 2009 John W. Eaton
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
## @deftypefn {Function File} {} xlabel (@var{string})
## @deftypefnx {Function File} {} ylabel (@var{string})
## @deftypefnx {Function File} {} zlabel (@var{string})
## @deftypefnx {Function File} {} xlabel (@var{h}, @var{string})
## Specify x, y, and z axis labels for the current figure.  If @var{h} is
## specified then label the axis defined by @var{h}.
## @seealso{plot, semilogx, semilogy, loglog, polar, mesh, contour,
## bar, stairs, title}
## @end deftypefn

## Author: jwe

function retval = xlabel (varargin)

  [h, varargin, nargin] = __plt_get_axis_arg__ ("xlabel", varargin{:});

  if (rem (nargin, 2) != 1)
    print_usage ();
  endif

  oldh = gca ();
  unwind_protect
    axes (h);
    tmp = __axis_label__ ("xlabel", varargin{:});
  unwind_protect_cleanup
    axes (oldh);
  end_unwind_protect

  if (nargout > 0)
    retval = tmp;
  endif

endfunction
