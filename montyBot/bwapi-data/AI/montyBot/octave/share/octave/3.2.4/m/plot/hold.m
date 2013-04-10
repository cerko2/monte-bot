## Copyright (C) 2005, 2006, 2007, 2008, 2009 John W. Eaton
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
## @deftypefn  {Function File} {} hold
## @deftypefnx {Function File} {} hold @var{state}
## @deftypefnx {Function File} {} hold (@var{hax}, @dots{})
## Toggle or set the 'hold' state of the plotting engine which determines
## whether new graphic objects are added to the plot or replace the existing
## objects.  
## 
## @table @code
## @item hold on
## Retain plot data and settings so that subsequent plot commands are displayed
## on a single graph.
##
## @item hold off
## Clear plot and restore default graphics settings before each new plot
## command.  (default).
##
## @item hold
## Toggle the current 'hold' state.
## @end table
## 
## When given the additional argument @var{hax}, the hold state is modified
## only for the given axis handle.
##
## To query the current 'hold' state use the @code{ishold} function.
## @seealso{ishold, cla, newplot, clf}
## @end deftypefn

function hold (varargin)

  if (nargin > 0 && numel (varargin{1}) == 1 && ishandle (varargin{1})
      && strcmp (get (varargin{1}, "type"), "axes"))
    [ax, varargin, nargs] = __plt_get_axis_arg__ ("hold", varargin{:});
  elseif (nargin > 0 && numel (varargin{1}) > 1 && ishandle (varargin{1}))
    print_usage ();
  else
    ax = gca ();
    fig = gcf ();
    nargs = numel (varargin);
  endif

  if (nargs == 0)
    turn_hold_off = ishold (ax);
  elseif (nargs == 1)
    state = varargin{1};
    if (ischar (state))
      if (strcmpi (state, "off"))
	turn_hold_off = true;
      elseif (strcmpi (state, "on"))
	turn_hold_off = false;
      else
	error ("hold: invalid hold state");
      endif
    endif
  else
    print_usage ();
  endif

  if (turn_hold_off)
    set (ax, "nextplot", "replace");
  else
    set (ax, "nextplot", "add");
    set (fig, "nextplot", "add");
  endif

endfunction

%!demo
%! clf
%! A = rand (100);
%! [X, Y] = find (A > 0.9);
%! imshow (A)
%! hold on
%! plot (X, Y, 'o')
%! hold off

%!demo
%! clf
%! hold on
%! imagesc(1./hilb(4));
%! plot (1:4, "-s")
%! hold off

%!demo
%! clf
%! hold on
%! imagesc(1./hilb(2));
%! imagesc(1./hilb(4));
%! hold off

%!demo
%! clf
%! hold on
%! plot (1:4, "-s")
%! imagesc(1./hilb(4));
%! hold off

%!demo
%! clf
%! colormap (jet)
%! t = linspace (-3, 3, 50);
%! [x, y] = meshgrid (t, t);
%! z = peaks (x, y);
%! contourf (x, y, z, 10);
%! hold ("on");
%! plot (vec (x), vec (y), "^");
%! patch ([-1.0 1.0 1.0 -1.0 -1.0], [-1.0 -1.0 1.0 1.0 -1.0], "red");
%! xlim ([-2.0 2.0]);
%! ylim ([-2.0 2.0]);
%! colorbar ("SouthOutside");
%! title ("Test script for some plot functions");

