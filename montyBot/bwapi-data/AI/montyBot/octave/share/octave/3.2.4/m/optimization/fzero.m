## Copyright (C) 2008, 2009 VZLU Prague, a.s.
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
##
## Author: Jaroslav Hajek <highegg@gmail.com>

## -*- texinfo -*-
## @deftypefn {Function File} {[@var{x}, @var{fval}, @var{info}, @var{output}] =} fzero (@var{fun}, @var{x0}, @var{options})
## Find a zero point of a univariate function.  @var{fun} should be a function
## handle or name.  @var{x0} specifies a starting point.  @var{options} is a
## structure specifying additional options.  Currently, @code{fzero}
## recognizes these options: @code{"FunValCheck"}, @code{"OutputFcn"},
## @code{"TolX"}, @code{"MaxIter"}, @code{"MaxFunEvals"}. 
## For description of these options, see @ref{doc-optimset,,optimset}.
## 
## On exit, the function returns @var{x}, the approximate zero point
## and @var{fval}, the function value thereof.
## @var{info} is an exit flag that can have these values:
## @itemize
## @item 1
## The algorithm converged to a solution.
## @item 0
## Maximum number of iterations or function evaluations has been exhausted.
## @item -1
## The algorithm has been terminated from user output function.
## @item -2 
## A general unexpected error.
## @item -3
## A non-real value encountered.
## @item -4
## A NaN value encountered.
## @end itemize
## @seealso{optimset, fsolve} 
## @end deftypefn

## This is essentially the ACM algorithm 748: Enclosing Zeros of
## Continuous Functions due to Alefeld, Potra and Shi, ACM Transactions
## on Mathematical Software, Vol. 21, No. 3, September 1995. Although
## the workflow should be the same, the structure of the algorithm has
## been transformed non-trivially; instead of the authors' approach of
## sequentially calling building blocks subprograms we implement here a
## FSM version using one interior point determination and one bracketing
## per iteration, thus reducing the number of temporary variables and
## simplifying the algorithm structure. Further, this approach reduces
## the need for external functions and error handling. The algorithm has
## also been slightly modified.

## PKG_ADD: __all_opts__ ("fzero");

function [x, fval, info, output] = fzero (fun, x0, options = struct ())

  ## Get default options if requested.
  if (nargin == 1 && ischar (fun) && strcmp (fun, 'defaults'))
    x = optimset ("MaxIter", Inf, "MaxFunEvals", Inf, "TolX", 0, \
    "OutputFcn", [], "FunValCheck", "off");
    return;
  endif

  if (nargin < 2 || nargin > 3)
    print_usage ();
  endif

  if (ischar (fun))
    fun = str2func (fun);
  endif

  ## TODO
  ## displev = optimget (options, "Display", "notify");
  funvalchk = strcmpi (optimget (options, "FunValCheck", "off"), "on");
  outfcn = optimget (options, "OutputFcn");
  tolx = optimget (options, "TolX", 0);
  maxiter = optimget (options, "MaxIter", Inf);
  maxfev = optimget (options, "MaxFunEvals", Inf);

  persistent mu = 0.5;

  if (funvalchk)
    ## Replace fun with a guarded version.
    fun = @(x) guarded_eval (fun, x);
  endif

  ## The default exit flag if exceeded number of iterations.
  info = 0;
  niter = 0;
  nfev = 0;

  x = fval = a = fa = b = fb = NaN;

  ## Prepare...
  a = x0(1);
  fa = fun (a); 
  nfev = 1;
  if (length (x0) > 1)
    b = x0(2);
    fb = fun (b);
    nfev += 1;
  else
    ## Try to get b.
    if (a == 0)
      aa = 1;
    else
      aa = a;
    endif
    for b = [0.9*aa, 1.1*aa, aa-1, aa+1, 0.5*aa 1.5*aa, -aa, 2*aa, -10*aa, 10*aa]
      fb = fun (b); nfev += 1;
      if (sign (fa) * sign (fb) <= 0)
        break;
      endif
    endfor
  endif

  if (b < a)
    u = a;
    a = b;
    b = u;
 
    fu = fa;
    fa = fb;
    fb = fu;
  endif

  if (! (sign (fa) * sign (fb) <= 0))
    error ("fzero:bracket", "fzero: not a valid initial bracketing");
  endif

  itype = 1;

  if (abs (fa) < abs (fb))
    u = a; fu = fa;
  else
    u = b; fu = fb;
  endif

  d = e = u;
  fd = fe = fu;
  mba = mu*(b - a);
  while (niter < maxiter && nfev < maxfev)
    switch (itype)
    case 1
      ## The initial test.
      if (b - a <= 2*(2 * abs (u) * eps + tolx))
        x = u; fval = fu;
        info = 1;
        break;
      endif
      if (abs (fa) <= 1e3*abs (fb) && abs (fb) <= 1e3*abs (fa))
        ## Secant step.
        c = u - (a - b) / (fa - fb) * fu;
      else
        ## Bisection step.
        c = 0.5*(a + b);
      endif
      d = u; fd = fu;
      itype = 5;
    case {2, 3}
      l = length (unique ([fa, fb, fd, fe]));
      if (l == 4)
        ## Inverse cubic interpolation.
        q11 = (d - e) * fd / (fe - fd);
        q21 = (b - d) * fb / (fd - fb);
        q31 = (a - b) * fa / (fb - fa);
        d21 = (b - d) * fd / (fd - fb);
        d31 = (a - b) * fb / (fb - fa);
        q22 = (d21 - q11) * fb / (fe - fb);
        q32 = (d31 - q21) * fa / (fd - fa);
        d32 = (d31 - q21) * fd / (fd - fa);
        q33 = (d32 - q22) * fa / (fe - fa);
        c = a + q31 + q32 + q33;
      endif
      if (l < 4 || sign (c - a) * sign (c - b) > 0)
        ## Quadratic interpolation + newton.
        a0 = fa;
        a1 = (fb - fa)/(b - a);
        a2 = ((fd - fb)/(d - b) - a1) / (d - a);
        ## Modification 1: this is simpler and does not seem to be worse.
        c = a - a0/a1;
        if (a2 != 0)
          c = a - a0/a1;
          for i = 1:itype
            pc = a0 + (a1 + a2*(c - b))*(c - a);
            pdc = a1 + a2*(2*c - a - b);
            if (pdc == 0)
              c = a - a0/a1;
              break;
            endif
            c -= pc/pdc;
          endfor
        endif
      endif
      itype += 1; 
    case 4
      ## Double secant step.
      c = u - 2*(b - a)/(fb - fa)*fu;
      ## Bisect if too far.
      if (abs (c - u) > 0.5*(b - a))
        c = 0.5 * (b + a);
      endif
      itype = 5;
    case 5
      ## Bisection step.
      c = 0.5 * (b + a);
      itype = 2;
    endswitch

    ## Don't let c come too close to a or b.
    delta = 2*0.7*(2 * abs (u) * eps + tolx);
    if ((b - a) <= 2*delta)
      c = (a + b)/2;
    else
      c = max (a + delta, min (b - delta, c));
    endif

    ## Calculate new point.
    x = c;
    fval = fc = fun (c); 
    niter ++; nfev ++;

    ## Modification 2: skip inverse cubic interpolation if
    ## nonmonotonicity is detected.
    if (sign (fc - fa) * sign (fc - fb) >= 0)
      ## The new point broke monotonicity. 
      ## Disable inverse cubic.
      fe = fc;
    else
      e = d; fe = fd;
    endif

    ## Bracketing.
    if (sign (fa) * sign (fc) < 0)
      d = b; fd = fb;
      b = c; fb = fc;
    elseif (sign (fb) * sign (fc) < 0)
      d = a; fd = fa;
      a = c; fa = fc;
    elseif (fc == 0)
      a = b = c; fa = fb = fc;
      info = 1;
      break;
    else
      ## This should never happen.
      error ("fzero:bracket", "fzero: zero point is not bracketed");
    endif

    ## If there's an output function, use it now.
    if (outfcn)
      optv.funccount = niter + 2;
      optv.fval = fval;
      optv.iteration = niter;
      if (outfcn (x, optv, "iter"))
        info = -1;
        break;
      endif
    endif

    if (abs (fa) < abs (fb))
      u = a; fu = fa;
    else
      u = b; fu = fb;
    endif
    if (b - a <= 2*(2 * abs (u) * eps + tolx))
      info = 1;
      break;
    endif

    ## Skip bisection step if successful reduction.
    if (itype == 5 && (b - a) <= mba)
      itype = 2;
    endif
    if (itype == 2)
      mba = mu * (b - a);
    endif
  endwhile

  output.iterations = niter;
  output.funcCount = niter + 2;
  output.bracket = [a, b];
  output.bracketf = [fa, fb];

endfunction

## An assistant function that evaluates a function handle and checks for
## bad results.
function fx = guarded_eval (fun, x)
  fx = fun (x);
  fx = fx(1);
  if (! isreal (fx))
    error ("fzero:notreal", "fzero: non-real value encountered"); 
  elseif (isnan (fx))
    error ("fzero:isnan", "fzero: NaN value encountered"); 
  endif
endfunction

%!assert(fzero(@cos, [0, 3]), pi/2, 10*eps)
%!assert(fzero(@(x) x^(1/3) - 1e-8, [0,1]), 1e-24, 1e-22*eps)
