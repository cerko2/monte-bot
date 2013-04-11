## Copyright (C) 2007, 2008, 2009 John W. Eaton, Shai Ayal, Kai Habel
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
## @deftypefn {Function File} {[@var{h}, @var{fail}] =} __patch__ (@var{p}, @dots{})
## Undocumented internal function.
## @end deftypefn

## __patch__ (p, x, y, c)
## Create patch object from x and y with color c and parent p.
## Return handle to patch object.

## Author: Kai Habel

function [h, failed] = __patch__ (p, varargin)

  failed = false;

  if (isstruct (varargin{1}))
    if (isfield (varargin{1}, "vertices") && isfield (varargin{1}, "faces"))
      args{1} = "faces";
      args{2} = getfield(varargin{1}, "faces");
      args{3} = "vertices";
      args{4} = getfield(varargin{1}, "vertices");
      args{5} = "facevertexcdata";
      if (isfield (varargin{1}, "facevertexcdata"))
	args{6} = getfield(varargin{1}, "facevertexcdata");
      else
	args{6} = [];
      endif
      args = [args; varargin(2:end)];
      args = setdata (args);
    else
      failed = true;
    endif
  elseif (isnumeric (varargin{1}))
    if (nargin < 3 || ! isnumeric (varargin{2}))
      failed = true;
    else
      x = varargin{1};
      y = varargin{2};
      iarg = 3;

      if (nargin > 3 && ndims (varargin{3}) == 2 && ndims (x) == 2
	  && size_equal(x, varargin{3}) && !ischar(varargin{3}))
	z = varargin{3};
	iarg++;
      else
	z = [];
      endif

      if (isvector (x))
	x = x(:);
	y = y(:);
	z = z(:);
      endif
      args{1} = "xdata";
      args{2} = x;
      args{3} = "ydata";
      args{4} = y;
      args{5} = "zdata";
      args{6} = z;

      if (isnumeric (varargin{iarg}))
	c = varargin{iarg};
	iarg++;

	if (ndims (c) == 3 && size (c, 2) == 1)
	  c = permute (c, [1, 3, 2]);
	endif

	if (isvector (c) && numel (c) == columns (x))
	  if (isnan (c))
	    args{7} = "facecolor";
	    args{8} = [1, 1, 1];
	    args{9} = "cdata";
	    args{10} = c;
	  elseif (isnumeric (c))
	    args{7} = "facecolor";
	    args{8} = "flat";
	    args{9} = "cdata";
	    args{10} = c;
	  else
	    error ("patch: color value not valid");
	  endif
	elseif (size (c, ndims (c)) == 3)
	  args{7} = "facecolor";
	  args{8} = "flat";
	  args{9} = "cdata";
	  args{10} = c;
	else
	  ## Color Vectors
	  if (rows (c) != rows (x) || rows (c) != length (y))
	    error ("patch: size of x, y, and c must be equal")
	  else
	    args{7} = "facecolor";
	    args{8} = "interp";
	    args{9} = "cdata";
	    args{10} = [];
	  endif
	endif
      elseif (ischar (varargin{iarg}) && rem (nargin - iarg, 2) != 0)
	## Assume that any additional argument over an even number is
	## color string.
	args{7} = "facecolor";
	args{8} =  tolower (varargin{iarg});
	args{9} = "cdata";
	args{10} = [];
	iarg++;
      else
	args{7} = "facecolor";
	args{8} = [0, 1, 0];
	args{9} = "cdata";
	args{10} = [];
      endif

      args = [args, varargin(iarg:end)];
      args = setvertexdata (args);
    endif
  else
    args = varargin;
    if (any(cellfun (@(x) strcmpi(x,"faces") || strcmpi(x, "vertices"), args)))
      args = setdata (args);
    else
      args = setvertexdata (args);
    endif
  endif

  if (!failed)
    h = __go_patch__ (p, args {:});

    ## Setup listener functions
    addlistener (h, "xdata", @update_data);
    addlistener (h, "ydata", @update_data);
    addlistener (h, "zdata", @update_data);
    addlistener (h, "cdata", @update_data);

    addlistener (h, "faces", @update_fvc);
    addlistener (h, "vertices", @update_fvc);
    addlistener (h, "facevertexcdata", @update_fvc);
  endif
endfunction

function args = delfields(args, flds)
  idx = cellfun (@(x) any (strcmpi (x, flds)), args);
  idx = idx | [false, idx(1:end-1)];
  args (idx) = [];
endfunction

function args = setdata (args)
  args = delfields (args, {"xdata", "ydata", "zdata", "cdata"});
  nargs = length (args);
  idx = find (cellfun (@(x) strcmpi (x, "faces"), args)) + 1;
  if (idx > nargs)
    faces = [];
  else
    faces = args {idx};
  endif
  idx = find (cellfun (@(x) strcmpi (x, "vertices"), args)) + 1;
  if (idx > nargs)
    vert = [];
  else
    vert = args {idx};
  endif
  idx = find (cellfun (@(x) strcmpi (x, "facevertexcdata"), args)) + 1;
  if (isempty(idx) || idx > nargs)
    fvc = [];
  else
    fvc = args {idx};
  endif
  idx = find (cellfun (@(x) strcmpi (x, "facecolor"), args)) + 1;
  if (isempty(idx) || idx > nargs)
    if (!isempty (fvc))
      fc = "flat";
    else
      fc = [0, 1, 0];
    endif
    args = {"facecolor", fc, args{:}};
  else
    fc = args {idx};
  endif

  nr = size (faces, 2);
  nc = size (faces, 1);
  idx = faces .';
  t1 = isnan (idx);
  if (any (t1(:)))
    t2 = find (t1 != t1([2:end,end],:));
    idx (t1) = idx (t2 (cell2mat (cellfun (@(x) x(1)*ones(1,x(2)),
		mat2cell ([1 : nc; sum(t1)], 2, ones(1,nc)), 
					   "UniformOutput", false))));
  endif
  x = reshape (vert(:,1)(idx), size (idx));
  y = reshape (vert(:,2)(idx), size (idx));
  if (size(vert,2) > 2)
    z = reshape (vert(:,3)(idx), size (idx));
  else
    z = [];
  endif

  if (ischar (fc) && (strcmpi (fc, "flat") || strcmpi (fc, "interp")))
    if (size(fvc, 1) == nc || size (fvc, 1) == 1)
      c = reshape (fvc, [1, size(fvc)]);
    else
      if (size(fvc, 2) == 3)
	c = cat(3, reshape (fvc(idx, 1), size(idx)),
		reshape (fvc(idx, 2), size(idx)),
		reshape (fvc(idx, 3), size(idx)));
      else
	c = reshape (fvc(idx), size(idx));
      endif
    endif
  else
    c = [];
  endif
  args = {"xdata", x, "ydata", y, "zdata", z, "cdata", c, args{:}};
endfunction

function args = setvertexdata (args)
  args = delfields (args, {"vertices", "faces", "facevertexcdata"});
  nargs = length (args);
  idx = find (cellfun (@(x) strcmpi (x, "xdata"), args)) + 1;
  if (idx > nargs)
    x = [];
  else
    x = args {idx};
  endif
  idx = find (cellfun (@(x) strcmpi (x, "ydata"), args)) + 1;
  if (idx > nargs)
    y = [];
  else
    y = args {idx};
  endif
  idx = find (cellfun (@(x) strcmpi (x, "zdata"), args)) + 1;
  if (isempty(idx) || idx > nargs)
    z = [];
  else
    z = args {idx};
  endif
  idx = find (cellfun (@(x) strcmpi (x, "cdata"), args)) + 1;
  if (isempty(idx) || idx > nargs)
    c = [];
  else
    c = args {idx};
  endif
  idx = find (cellfun (@(x) strcmpi (x, "facecolor"), args)) + 1;
  if (isempty(idx) || idx > nargs)
    if (!isempty (c))
      fc = "flat";
    else
      fc = [0, 1, 0];
    endif
    args = {"facecolor", fc, args{:}};
  else
    fc = args {idx};
  endif

  [nr, nc] = size (x);
  if (!isempty (z))
    vert = [x(:), y(:), z(:)];
  else
    vert = [x(:), y(:)];
  endif
  faces = reshape (1:numel(x), rows (x), columns (x));
  faces = faces';

  if (ischar (fc) && (strcmpi (fc, "flat") || strcmpi (fc, "interp")))
    if (ndims (c) == 3)
      fvc = reshape (c, size (c, 1) * size (c, 2), size(c, 3));
    else
      fvc = c(:);
    endif
  else
    fvc = [];
  endif

  args = {"faces", faces, "vertices", vert, "facevertexcdata", fvc, args{:}};
endfunction

function update_data (h, d)
  update_handle (h, false);
endfunction

function update_fvc (h, d)
  update_handle (h, true);
endfunction

function update_handle (h, isfv)
  persistent recursive = false;

  if (! recursive)
    recursive = true;
    f = get (h);
    if (isfvc)
      set (h, setvertexdata ([fieldnames(f), struct2cell(f)].'(:)){:});
    else
      set (h, setdata ([fieldnames(f), struct2cell(f)].'(:)){:});
    endif
    recursive = false;
  endif
endfunction
