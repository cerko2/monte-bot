## Copyright (C) 2006, 2007, 2009 S�ren Hauberg
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

## Undocumented internal function.

## -*- texinfo -*-
## @deftypefn {Function File} {} __img_via_file__ (@var{x}, @var{y}, @var{im}, @var{zoom}, @var{command})
## Undocumented internal function.
## @end deftypefn

## Display an image by saving it to a file in PPM format and launching
## @var{command}.
##
## The @var{command} must be a format string containing @code{%s} and
## possibly @code{%f}.  The @code{%s} will be replaced by the filename
## of the image, and the @code{%f} will be replaced by @var{zoom}. The
## @var{x} and @var{y} arguments are ignored.

function __img_via_file__ (x, y, im, zoom, command)

  ppm_name = tmpnam ();
  saveimage (ppm_name, im, "ppm");

  rm = sprintf ("rm -f \"%s\"", ppm_name);

  if (isempty (command))
    ## Different image viewer commands.
    xv = sprintf ("xv -raw -expand %f \"%s\"", zoom, ppm_name);
    xloadimage = sprintf ("xloadimage -zoom %f \"%s\"", zoom*100, ppm_name);
    im_display = sprintf ("display -resize %f%% \"%s\"", zoom*100, ppm_name);
  
    ## Need to let the shell clean up the tmp file because we are putting
    ## the viewer in the background.
    status = system (sprintf ("( %s || %s || %s && %s ) > /dev/null 2>&1 &",
                              im_display, xv, xloadimage, rm));
  else
    ## Does the command support zooming?
    if (findstr (command, "%f"))
      command = sprintf (command, zoom, ppm_name);
    else
      command = sprintf (command, ppm_name);
    endif
    status = system (sprintf ("( %s && %s) > /dev/null 2>&1 &", command, rm));
  endif
  
  ## Did the system call fail?
  if (status != 0)
    error ("the image viewing command failed");
  endif

endfunction
