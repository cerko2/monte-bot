## Copyright (C) 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2002, 2003,
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
## @deftypefn {Function File} {} image (@var{img})
## @deftypefnx {Function File} {} image (@var{x}, @var{y}, @var{img})
## Display a matrix as a color image.  The elements of @var{x} are indices
## into the current colormap, and the colormap will be scaled so that the
## extremes of @var{x} are mapped to the extremes of the colormap.
##
## It first tries to use @code{gnuplot}, then @code{display} from 
## @code{ImageMagick}, then @code{xv}, and then @code{xloadimage}.
## The actual program used can be changed using the @code{image_viewer}
## function.
##
## The axis values corresponding to the matrix elements are specified in
## @var{x} and @var{y}.  If you're not using gnuplot 4.2 or later, these
## variables are ignored.
## @seealso{imshow, imagesc, colormap, image_viewer}
## @end deftypefn

## Author: Tony Richardson <arichard@stark.cc.oh.us>
## Created: July 1994
## Adapted-By: jwe

function retval = image (varargin)

  [ax, varargin, nargin] = __plt_get_axis_arg__ ("image", varargin{:});

  firstnonnumeric = Inf;
  for i = 1 : nargin
    if (! isnumeric (varargin{i}))
      firstnonnumeric = i;
      break;
    endif
  endfor

  if (nargin == 0 || firstnonnumeric == 1)
    img = loadimage ("default.img");
    x = y = [];
  elseif (nargin == 1 || firstnonnumeric == 2)
    img = varargin{1};
    x = y = [];
  elseif (nargin == 2 || firstnonnumeric == 3)
    print_usage ();
  else
    x = varargin{1};
    y = varargin{2};
    img = varargin{3};
    firstnonnumeric = 4;
  endif

  oldax = gca ();
  unwind_protect
    axes (ax);
    h = __img__ (x, y, img, varargin {firstnonnumeric:end});
    set (ax, "layer", "top");
  unwind_protect_cleanup
    axes (oldax);
  end_unwind_protect

  if (nargout > 0)
    retval = h;
  endif

endfunction
