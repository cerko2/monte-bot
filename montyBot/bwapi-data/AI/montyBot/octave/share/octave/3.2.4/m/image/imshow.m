## Copyright (C) 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2004, 2005,
##               2006, 2007, 2008, 2009 John W. Eaton
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
## @deftypefn {Function File} {} imshow (@var{im})
## @deftypefnx {Function File} {} imshow (@var{im}, @var{limits})
## @deftypefnx {Function File} {} imshow (@var{im}, @var{map})
## @deftypefnx {Function File} {} imshow (@var{rgb}, @dots{})
## @deftypefnx {Function File} {} imshow (@var{filename})
## @deftypefnx {Function File} {} imshow (@dots{}, @var{string_param1}, @var{value1}, @dots{})
## Display the image @var{im}, where @var{im} can be a 2-dimensional
## (gray-scale image) or a 3-dimensional (RGB image) matrix.
##
## If @var{limits} is a 2-element vector @code{[@var{low}, @var{high}]},
## the image is shown using a display range between @var{low} and
## @var{high}.  If an empty matrix is passed for @var{limits}, the
## display range is computed as the range between the minimal and the
## maximal value in the image.
##
## If @var{map} is a valid color map, the image will be shown as an indexed
## image using the supplied color map.
##
## If a file name is given instead of an image, the file will be read and
## shown.
##
## If given, the parameter @var{string_param1} has value
## @var{value1}.  @var{string_param1} can be any of the following:
## @table @samp
## @item "displayrange"
## @var{value1} is the display range as described above.
## @end table
## @seealso{image, imagesc, colormap, gray2ind, rgb2ind}
## @end deftypefn

## Author: Stefan van der Walt  <stefan@sun.ac.za>
## Author: Soren Hauberg <hauberg at gmail dot com>
## Adapted-By: jwe

function h = imshow (im, varargin)

  if (nargin == 0)
    print_usage ();
  endif

  display_range = NA;
  true_color = false;
  indexed = false;

  ## Get the image.
  if (ischar (im))
    ## Eventually, this should be imread.
    [im, map] = imread (im);
    indexed = true;
    colormap (map);
  endif

  if (! (isnumeric (im) && (ndims (im) == 2 || ndims (im) == 3)))
    error ("imshow: first argument must be an image or the filename of an image");
  endif

  if (ndims (im) == 2)
    if (! indexed)
      colormap (gray ());
    endif
  elseif (size (im, 3) == 3)
    if (ismember (class (im), {"uint8", "uint16", "double", "single"}))
      true_color = true;
    else
      error ("imshow: color image must be uint8, uint16, double, or single");
    endif
  else
    error ("imshow: expecting MxN or MxNx3 matrix for image");
  endif

  narg = 1;
  while (narg <= numel (varargin))
    arg = varargin{narg++};
    if (isnumeric (arg))
      if (numel (arg) == 2 || isempty (arg))
	display_range = arg;
      elseif (columns (arg) == 3)
	indexed = true;
	colormap (arg);
      elseif (! isempty (arg))
	error ("imshow: argument number %d is invalid", narg+1);
      endif
    elseif (ischar (arg))
      switch (arg)
	case "displayrange";
	  display_range = varargin{narg++};
	case {"truesize", "initialmagnification"}
	  warning ("image: zoom argument ignored -- use GUI features");
	otherwise
	  warning ("imshow: unrecognized property %s", arg);
	  narg++;
      endswitch
    else
      error ("imshow: argument number %d is invalid", narg+1);
    endif
  endwhile

  ## Set default display range if display_range not set yet.
  if (isempty (display_range))
    display_range = [min(im(:)), max(im(:))];
  elseif (isna (display_range))
    t = class (im);
    switch (t)
      case {"double", "single", "logical"}
	display_range = [0, 1];
      case {"int8", "int16", "int32", "uint8", "uint16", "uint32"}
	## For compatibility, uint8 data should not be handled as
	## double.  Doing so is a quick fix to allow the images to be
	## displayed correctly.
	display_range = double ([intmin(t), intmax(t)]);
      otherwise
	error ("imshow: invalid data type for image");
    endswitch
  endif

  ## Check for complex images.
  if (iscomplex (im))
    warning ("imshow: only showing real part of complex image");
    im = real (im);
  endif
  
  nans = isnan (im(:));
  if (any (nans))
    warning ("Octave:imshow-NaN",
	     "imshow: pixels with NaN or NA values are set to minimum pixel value");
    im(nans) = display_range(1);
  endif

  ## This is for compatibility.
  if (! (indexed || (true_color && isinteger (im))) || islogical (im))
    im = double (im);
  endif

  ## Scale the image to the interval [0, 1] according to display_range.
  if (! (true_color || indexed || islogical (im)))
    low = display_range(1);
    high = display_range(2);
    im = (im-low)/(high-low);
    im(im < 0) = 0;
    im(im > 1) = 1;
  endif

  if (true_color || indexed)
    tmp = image ([], [], im);
  else
    tmp = image (round ((rows (colormap ()) - 1) * im));
  endif
  set (gca (), "visible", "off");
  axis ("image");

  if (nargout > 0)
    h = tmp;
  endif

endfunction

%!error imshow ()                           # no arguments
%!error imshow ({"cell"})                   # No image or filename given
%!error imshow (ones(4,4,4))                # Too many dimensions in image

%!demo
%!  imshow ("default.img");

%!demo
%!  imshow ("default.img");
%!  colormap ("autumn");

%!demo
%!  [I, M] = imread ("default.img");
%!  imshow (I, M);

%!demo
%!  [I, M] = imread ("default.img");
%!  [R, G, B] = ind2rgb (I, M);
%!  imshow (cat(3, R, G*0.5, B*0.8));

%!demo
%!  imshow (rand (100, 100));

%!demo
%!  imshow (rand (100, 100, 3));

%!demo
%!  imshow (100*rand (100, 100, 3));

%!demo
%!  imshow (rand (100, 100));
%!  colormap (jet);
