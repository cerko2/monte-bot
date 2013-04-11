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
## @deftypefn {Function File} {} triplot (@var{tri}, @var{x}, @var{y})
## @deftypefnx {Function File} {} triplot (@var{tri}, @var{x}, @var{y}, @var{linespec})
## @deftypefnx {Function File} {@var{h} =} triplot (@dots{})
## Plot a triangular mesh in 2D.  The variable @var{tri} is the triangular
## meshing of the points @code{(@var{x}, @var{y})} which is returned from
## @code{delaunay}.  If given, the @var{linespec} determines the properties
## to use for the lines.  The output argument @var{h} is the graphic handle
## to the plot.
## @seealso{plot, trimesh, delaunay}
## @end deftypefn

function h = triplot (tri, x, y, varargin)

  if (nargin < 3)
    print_usage ();
  endif

  idx = tri(:, [1, 2, 3, 1]).';
  nt = size (tri, 1);
  if (nargout > 0)
    h = plot ([x(idx); NaN*ones(1, nt)](:),
	      [y(idx); NaN*ones(1, nt)](:), varargin{:});
  else
    plot ([x(idx); NaN*ones(1, nt)](:),
	  [y(idx); NaN*ones(1, nt)](:), varargin{:});
  endif
endfunction

%!demo
%! rand ('state', 2)
%! x = rand (20, 1);
%! y = rand (20, 1);
%! tri = delaunay (x, y);
%! triplot (tri, x, y);
