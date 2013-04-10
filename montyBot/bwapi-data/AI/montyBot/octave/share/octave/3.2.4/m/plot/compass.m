## Copyright (C) 2007, 2008, 2009 David Bateman
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
## @deftypefn {Function File} {} compass (@var{u}, @var{v})
## @deftypefnx {Function File} {} compass (@var{z})
## @deftypefnx {Function File} {} compass (@dots{}, @var{style})
## @deftypefnx {Function File} {} compass (@var{h}, @dots{})
## @deftypefnx {Function File} {@var{h} =} compass (@dots{})
##
## Plot the @code{(@var{u}, @var{v})} components of a vector field emanating
## from the origin of a polar plot.  If a single complex argument @var{z} is 
## given, then @code{@var{u} = real (@var{z})} and @code{@var{v} = imag 
## (@var{z})}.
##
## The style to use for the plot can be defined with a line style @var{style}
## in a similar manner to the line styles used with the @code{plot} command.
##
## The optional return value @var{h} provides a list of handles to the 
## the parts of the vector field (body, arrow and marker).
##
## @example
## @group
## a = toeplitz([1;randn(9,1)],[1,randn(1,9)]);
## compass (eig (a))
## @end group
## @end example
##
## @seealso{plot, polar, quiver, feather}
## @end deftypefn

function retval = compass (varargin)

  [h, varargin, nargin] = __plt_get_axis_arg__ ("compass", varargin{:});

  arrowsize = 0.25;
  firstnonnumeric = Inf;
  for i = 1:nargin
    if (! isnumeric (varargin{i}))
      firstnonnumeric = i;
      break;
    endif
  endfor

  if (nargin < 2 || firstnonnumeric < 2)
    ioff = 2;
    z = varargin {1} (:) .';
    u = real (z);
    v = imag (z);
  else
    ioff = 3;
    u = varargin {1} (:) .';
    v = varargin {2} (:) .';
  endif

  line_spec = "b-";
  while (ioff <= nargin)
    arg = varargin{ioff++};
    if ((ischar (arg) || iscell (arg)) && ! have_line_spec)
      [linespec, valid] = __pltopt__ ("compass", arg, false);
      if (valid)
	line_spec = arg;
	break;
      else
	error ("compass: invalid linespec");
      endif
    else
      error ("compass: unrecognized argument");
    endif
  endwhile

  ## Matlab draws compass plots, with the arrow head as one continous 
  ## line, and each arrow separately. This is completely different than 
  ## quiver and quite ugly.
  n = length (u);
  xend = u;
  xtmp = u .* (1 - arrowsize);
  yend = v;
  ytmp = v .* (1 - arrowsize);
  x = [zeros(1, n); xend; xtmp  - v * arrowsize / 3; xend; ...
       xtmp + v * arrowsize / 3];
  y = [zeros(1, n); yend; ytmp  + u * arrowsize / 3; yend; ...
       ytmp - u * arrowsize / 3];
  [r, p] = cart2pol (x, y);

  oldh = gca ();
  unwind_protect
    axes (h);
    newplot ();
    hlist = polar (h, r, p, line_spec);
  unwind_protect_cleanup
    axes (oldh);
  end_unwind_protect

  if (nargout > 0)
    retval = hlist;
  endif

endfunction

%!demo
%! a = toeplitz([1;randn(9,1)],[1,randn(1,9)]);
%! compass (eig (a))
