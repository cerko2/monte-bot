## Copyright (C) 1995, 1996, 1997, 2005, 2007, 2008 Kurt Hornik
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
## @deftypefn {Function File} {} t_inv (@var{x}, @var{n})
## For each component of @var{x}, compute the quantile (the inverse of
## the CDF) at @var{x} of the t (Student) distribution with parameter
## @var{n}.
## @end deftypefn

## For very large n, the "correct" formula does not really work well,
## and the quantiles of the standard normal distribution are used
## directly.

## Author: KH <Kurt.Hornik@wu-wien.ac.at>
## Description: Quantile function of the t distribution

## Deprecated in version 3.0

function inv = t_inv (varargin)

  persistent warned = false;
  if (! warned)
    warned = true;
    warning ("Octave:deprecated-function",
             "t_inv is obsolete and will be removed from a future version of Octave; please use tinv instead");
  endif

 inv =  tinv (varargin{:});

endfunction
