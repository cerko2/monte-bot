## Copyright (C) 2007, 2008 John W. Eaton
## Copyright (C) 2009 VZLU Prague
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
## @deftypefn {Function File} {} optimset ()
## @deftypefnx {Function File} {} optimset (@var{par}, @var{val}, @dots{})
## @deftypefnx {Function File} {} optimset (@var{old}, @var{par}, @var{val}, @dots{})
## @deftypefnx {Function File} {} optimset (@var{old}, @var{new})
## Create options struct for optimization functions.
## @end deftypefn

function retval = optimset (varargin)

  nargs = nargin ();

  ## Add more as needed.
  opts = __all_opts__ ();

  if (nargs == 0)
    if (nargout == 0)
      ## Display possibilities.
      puts ("\nAll possible optimization options:\n\n");
      printf ("  %s\n", opts{:});
      puts ("\n");
    else
      ## Return empty structure.
      ## We're incompatible with Matlab at this point.
      retval = struct ();
    endif
  elseif (nargs == 1 && ischar (varargin{1}))
    ## Return defaults for named function.
    fcn = varargin{1};
    try
      retval = feval (fcn, 'defaults');
    catch
      error ("no defaults for function `%s'", fcn);
    end_try_catch
  elseif (nargs == 2 && isstruct (varargin{1}) && isstruct (varargin{2}))
    ## Set slots in old from nonempties in new.  Should we be checking
    ## to ensure that the field names are expected?
    old = varargin{1};
    new = varargin{2};
    fnames = fieldnames (old);
    ## skip validation if we're in the internal query
    validation = ! isempty (opts);
    for [val, key] = new
      if (validation)
        ## Case insensitive lookup in all options.
        i = lookup (opts, key, "i");
        ## Validate option.
        if (i > 0 && strcmpi (opts{i}, key))
          ## Use correct case.
          key = opts{i};
        else
          warning ("unrecognized option: %s", key);
        endif
      endif
      old.(key) = val;
    endfor
    retval = old;
  elseif (rem (nargs, 2) && isstruct (varargin{1}))
    ## Set values in old from name/value pairs.
    retval = optimset (varargin{1}, struct (varargin{2:end}));
  elseif (rem (nargs, 2) == 0)
    ## Create struct.  Default values are replaced by those specified by
    ## name/value pairs.
    retval = optimset (struct (), struct (varargin{:}));
  else
    print_usage ();
  endif

endfunction

%!assert (optimget (optimset ('tolx', 1e-2), 'tOLx'), 1e-2)
%!assert (isfield (optimset ('tolFun', 1e-3), 'TolFun'))
