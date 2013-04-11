%% Clear and Close Figures
clear ; close all; clc

%% Check the input
if (size(argv(),1) != 2) 
	fprintf('ERROR: You need to specify 2 arguments: <file_with_training_set> <order_of_h(x)_polynomial>');
	quit();
endif;
if (exist(argv(){1},'file') == false) 
	fprintf('ERROR: Specified input file not found.');
	quit();
endif;
if (str2num(argv(){2}) < 1) 
	fprintf('ERROR: You need to specify the order of h(X) polynomial >= 1.');
	quit();
endif;

%% Load Data
data = load(argv(){1});
X = data(:,1:size(data,2)-1);
y = data(:,size(data,2));
m = length(y);

% Convert X into higher order polynomial
if (str2num(argv(){2}) >= 2)
	newX = X;
	for order = 2:str2num(argv(){2})
		newX = [newX X.^order];
	endfor;
	X = newX;
endif;

% Add intercept term to X
X = [ones(m, 1) X];

% Initialize Theta 
theta = zeros(size(X,2),1);

% Use fminunc function to run gradient descent
options = optimset('GradObj', 'on', 'MaxIter', 100);
[theta, cost] = fminunc(@(t)(cost_function(t, X, y)), theta, options);

% Display gradient descent's result
fprintf('Theta (%f):\n',cost);
fprintf('%f\n', theta);
%computeCost(X, y, theta)
%cost

% DEBUG: Print out predictions and actual values
%[round(X*theta) y]

% DEBUG: Calculate the parameters from the normal equation
%theta = normalEqn(X, y);
% Display normal equation's result
%fprintf('Theta computed from the normal equations: \n');
%fprintf(' %f \n', theta);
%fprintf('\n');
%fprintf('COST:');
%computeCost(X, y, theta)


