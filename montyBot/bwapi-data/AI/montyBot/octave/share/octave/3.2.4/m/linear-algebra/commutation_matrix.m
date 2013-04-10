## Copyright (C) 1995, 1996, 1999, 2000, 2002, 2005, 2006, 2007, 2009
##               Kurt Hornik
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
## @deftypefn {Function File} {} commutation_matrix (@var{m}, @var{n})
## Return the commutation matrix
## @tex
##  $K_{m,n}$
## @end tex
## @ifnottex
##  K(m,n)
## @end ifnottex
##  which is the unique
## @tex
##  $m n \times m n$
## @end tex
## @ifnottex
##  @var{m}*@var{n} by @var{m}*@var{n}
## @end ifnottex
##  matrix such that
## @tex
##  $K_{m,n} \cdot {\rm vec} (A) = {\rm vec} (A^T)$
## @end tex
## @ifnottex
##  @math{K(m,n) * vec(A) = vec(A')}
## @end ifnottex
##  for all
## @tex
##  $m\times n$
## @end tex
## @ifnottex
##  @math{m} by @math{n}
## @end ifnottex
##  matrices
## @tex
##  $A$.
## @end tex
## @ifnottex
##  @math{A}.
## @end ifnottex
##
## If only one argument @var{m} is given,
## @tex
##  $K_{m,m}$
## @end tex
## @ifnottex
##  @math{K(m,m)}
## @end ifnottex
##  is returned.
##
## See Magnus and Neudecker (1988), Matrix differential calculus with
## applications in statistics and econometrics.
## @end deftypefn

## Author: KH <Kurt.Hornik@wu-wien.ac.at>
## Created: 8 May 1995
## Adapted-By: jwe

function k = commutation_matrix (m, n)

  if (nargin < 1 || nargin > 2)
    print_usage ();
  else
    if (! (isscalar (m) && m == round (m) && m > 0))
      error ("commutation_matrix: m must be a positive integer");
    endif
    if (nargin == 1)
      n = m;
    elseif (! (isscalar (n) && n == round (n) && n > 0))
      error ("commutation_matrix: n must be a positive integer");
    endif
  endif

  ## It is clearly possible to make this a LOT faster!
  k = zeros (m * n, m * n);
  for i = 1 : m
    for j = 1 : n
      k ((i - 1) * n + j, (j - 1) * m + i) = 1;
    endfor
  endfor

endfunction
