## Copyright (C) 2008, 2009 John W. Eaton
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
## @deftypefn {Function File} {} imwrite (@var{img}, @var{filename}, @var{fmt}, @var{p1}, @var{v1}, @dots{})
## @deftypefnx {Function File} {} imwrite (@var{img}, @var{map}, @var{filename}, @var{fmt}, @var{p1}, @var{v1}, @dots{})
## Write images in various file formats.
##
## If @var{fmt} is missing, the file extension (if any) of
## @var{filename} is used to determine the format.
##
## The parameter-value pairs (@var{p1}, @var{v1}, @dots{}) are optional.  Currently
## the following options are supported for @t{JPEG} images
##
## @table @samp
## @item Quality
## Sets the quality of the compression.  The corresponding value should be an
## integer between 0 and 100, with larger values meaning higher visual quality
## and less compression.
## @end table
##
## @seealso{imread, imfinfo}
## @end deftypefn

function imwrite (varargin)

  persistent accepted_formats = { "bmp", "gif", "jpg", "jpeg", ...
    "ras", "pbm", "pgm", "png", "ppm", "svg", "tif", "tiff" };

  img = [];
  map = [];
  fmt = "";

  if (nargin > 1 && isnumeric (varargin{1}))
    img = varargin{1};
    offset = 2;
    if (isnumeric (varargin{2}))
      map = varargin{2};
      if (isempty (map))
        error ("imwrite: colormap must not be empty");
      endif
      offset = 3;
    endif
    if (offset <= nargin && ischar (varargin{offset}))
      filename = varargin{offset};
      offset++;
      if (rem (nargin - offset, 2) == 0 && ischar (varargin{offset}))
        fmt = varargin{offset};
        offset++;
      endif
    else
      print_usage ();
    endif
    if (offset < nargin)
      has_param_list = 1;
      for ii = offset:2:(nargin - 1)
        options.(varargin{ii}) = varargin{ii + 1};
      endfor
    else
      has_param_list = 0;
    endif
  else
    print_usage ();
  endif

  filename = tilde_expand (filename);

  if (isempty (fmt))
    [d, n, fmt] = fileparts (filename);
    if (! isempty (fmt))
      fmt = fmt(2:end);
    endif
  endif

  if (issparse (img) || issparse (map))
    error ("imwrite: sparse images not supported");
  endif

  if (isempty (img))
    error ("imwrite: invalid empty image");
  endif

  if (! strcmp (fmt, accepted_formats))
    error ("imwrite: %s: unsupported or invalid image format", fmt);
  endif

  img_class = class (img);
  map_class = class (map);
  nd = ndims (img);

  if (isempty (map))
    if (any (strcmp (img_class, {"logical", "uint8", "uint16", "double"})))
      if ((nd == 2 || nd == 3) && strcmp (img_class, "double"))
        img = uint8 (img * 255);
      endif
      ## FIXME -- should we handle color images w/ alpha channel here?
      if (nd == 3 && size (img, 3) < 3)
        error ("imwrite: invalid dimensions for truecolor image");
      endif
      if (nd > 5)
        error ("imwrite: invalid %d-dimensional image data", nd);
      endif
    else
      error ("imwrite: %s: invalid class for truecolor image", img_class);
    endif
    if (has_param_list)
      __magick_write__ (filename, fmt, img, options);
    else
      __magick_write__ (filename, fmt, img);
    endif
  else
    if (any (strcmp (img_class, {"uint8", "uint16", "double"})))
      if (strcmp (img_class, "double"))
        img = uint8 (img - 1);
      endif
      if (nd != 2 && nd != 4)
        error ("imwrite: invalid size for indexed image");
      endif
    else
      error ("imwrite: %s: invalid class for indexed image data", img_class);
    endif
    if (isa (map, "double"))
      if (ndims (map) != 2 || size (map, 2) != 3)
        error ("imwrite: invalid size for colormap");
      endif
    else
      error ("imwrite: %s invalid class for indexed image colormap",
             class (map));
    endif

    ## FIXME -- we should really be writing indexed images here but
    ## __magick_write__ needs to be fixed to handle them.

    [r, g, b] = ind2rgb (img, map);
    tmp = uint8 (cat (3, r, g, b) * 255);

    if (has_param_list)
      __magick_write__ (filename, fmt, tmp, options);
      ## __magick_write__ (filename, fmt, img, map, options);
    else
      __magick_write__ (filename, fmt, tmp);
      ## __magick_write__ (filename, fmt, img, map);
    endif
  endif

endfunction
